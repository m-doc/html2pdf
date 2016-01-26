package html2pdf

import html2pdf.StreamUtil._
import html2pdf.logging.Log
import java.nio.file.Path
import org.mdoc.fshell.{ ProcessResult, Shell }
import org.mdoc.fshell.Shell.ShellSyntax
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

  def execCmd(cmd: String, args: String*): LogWriter[Task, String] = {
    val cmdLine = (cmd +: args).mkString(" ")
    Log.info(s"Executing $cmdLine") ++
      Process.await(Shell.readProcess(cmd, args.toList).runTask)(emitCmdResult).onFailure(t => Log.error(t.getMessage))
  }

  def execWkHtmlToPdf(input: String, output: Path): LogWriter[Task, Nothing] =
    execCmd("wkhtmltopdf-h2p.sh", input, output.toString).flatMapO { out =>
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
