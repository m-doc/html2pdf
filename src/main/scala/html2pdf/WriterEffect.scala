package html2pdf

import java.nio.file.Path

import html2pdf.StreamUtil._
import html2pdf.logging.Log
import org.http4s.Request
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream._

object WriterEffect {
  def createPdf(url: String): LogWriter[Task, ByteVector] =
    Log.info(s"Converting $url") ++
      tempFile("h2p-", ".pdf").flatMapO { pdf =>
        execWkHtmlToPdf(url, pdf) ++ readFile(pdf)
      }

  def deleteFile(path: Path): LogWriter[Task, Unit] =
    Log.info(s"Deleting ${path.toString}") ++
      StreamUtil.evalO(Effect.deleteFile(path))

  def emitCmdResult(res: Effect.CmdResult): LogWriter[Nothing, String] = {
    def logErr = Log.warn(res.err)
    def logStatus = Log.error(s"${res.cmd} exited with status ${res.status.toString}")
    Process.emitO(res.out) ++
      StreamUtil.runIf(res.err.nonEmpty)(logErr) ++
      StreamUtil.runIf(res.status != 0)(logStatus)
  }

  def emitRequest(req: Request): LogWriter[Nothing, Nothing] = {
    /*
    val method = req.method.renderString
    val uri = req.uri.renderString
    val remote = req.remoteAddr.fold("")(" from " + _)

    infoW(s"$method $uri")
    */
    ???
  }

  def execCmd(cmd: String, args: String*): LogWriter[Task, String] = {
    val cmdLine = (cmd +: args).mkString(" ")
    Log.info(s"Executing $cmdLine") ++
      Process.await(Effect.execCmd(cmd, args: _*))(emitCmdResult)
  }

  def execWkHtmlToPdf(input: String, output: Path): LogWriter[Task, Nothing] =
    execCmd("wkhtmltopdf-h2p.sh", input, output.toString).flatMapO { out =>
      val trimmed = out.trim
      StreamUtil.runIf(trimmed.nonEmpty)(Log.info(trimmed))
    }

  def readFile(path: Path): LogWriter[Task, ByteVector] = {
    val bufferSize = 8192
    Log.info(s"Reading file ${path.toString}") ++
      Process.liftW(Process.constant(bufferSize).through(nio.file.chunkR(path)))
  }

  def tempFile(prefix: String, suffix: String): LogWriter[Task, Path] =
    Process.await(Effect.createTempFile(prefix, suffix)) { path =>
      val msg = s"Created temporary file ${path.toString}"
      Log.info(msg) ++ Process.emitO(path).onComplete(deleteFile(path).ignoreO)
    }
}
