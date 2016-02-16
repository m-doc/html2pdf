package org.mdoc.html2pdf

import java.nio.file.{ Path, Paths }
import org.mdoc.fshell.{ ProcessResult, Shell }
import org.mdoc.fshell.Shell.ShellSyntax
import org.mdoc.html2pdf.StreamUtil._
import org.mdoc.html2pdf.logging.Log
import org.mdoc.rendering.engines.wkhtmltopdf
import scalaz.concurrent.Task
import scalaz.stream._
import scodec.bits.ByteVector

object WriterEffect {
  def createPdf(url: String): LogWriter[Task, ByteVector] =
    Log.info(s"Converting $url") ++
      tempFile("h2p-", ".pdf").flatMapO { pdf =>
        execWkHtmlToPdf(url, pdf) ++ readFile(pdf)
      }

  def deleteFile(path: Path): LogWriter[Task, Unit] =
    Log.info(s"Deleting ${path.toString}") ++
      StreamUtil.evalO(Shell.delete(path).runTask)

  def emitCmdResult(res: ProcessResult): LogWriter[Nothing, String] = {
    def logError = Log.warn(res.err)
    def logStatus = Log.error(s"${res.command} exited with status ${res.status.toString}")

    Process.emitO(res.out) ++
      StreamUtil.runIf(res.err.nonEmpty)(logError) ++
      StreamUtil.runIf(res.status != 0)(logStatus)
  }

  def execCmd(cmd: Shell[ProcessResult]): LogWriter[Task, String] = {
    Process.await(cmd.throwOnError.runTask)(emitCmdResult).onFailure(t => Log.error(t.getMessage))
  }

  def execWkHtmlToPdf(input: String, output: Path): LogWriter[Task, Nothing] =
    execCmd(wkhtmltopdf.execWkhtmltopdf(input, output, Paths.get("."))).flatMapO { out =>
      val trimmed = out.trim
      StreamUtil.runIf(trimmed.nonEmpty)(Log.info(trimmed))
    }

  def readFile(path: Path): LogWriter[Task, ByteVector] =
    Log.info(s"Reading file ${path.toString}") ++
      writer.liftO(Process.constant(bufferSize).through(nio.file.chunkR(path)))

  def tempFile(prefix: String, suffix: String): LogWriter[Task, Path] =
    Process.await(Shell.createTempFile(prefix, suffix).runTask) { path =>
      val msg = s"Created temporary file ${path.toString}"
      Log.info(msg) ++ Process.emitO(path).onComplete(deleteFile(path).ignoreO)
    }
}
