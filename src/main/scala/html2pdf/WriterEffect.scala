package html2pdf

import java.nio.file.Path

import html2pdf.StreamUtil._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream.Writer

object WriterEffect {
  def createPdf(url: String): Writer[Task, LogEntry, ByteVector] =
    Log.infoW(s"Converting $url") ++
      tempFile("h2p-", ".pdf").flatMapO { pdf =>
        execWkHtmlToPdf(url, pdf) ++ readFile(pdf)
      }

  def deleteFile(path: Path): Writer[Task, LogEntry, Unit] =
    Log.infoW(s"Deleting ${path.toString}") ++
      evalO(Effect.deleteFile(path))

  def emitCmdResult(res: Effect.CmdResult): Writer[Nothing, LogEntry, String] = {
    def errLog = Log.warnW(res.err)
    def statusLog = Log.errorW(s"${res.cmd} exited with status ${res.status.toString}")
    emitO(res.out) ++ runIf(res.err.nonEmpty)(errLog) ++ runIf(res.status != 0)(statusLog)
  }

  def execCmd(cmd: String, args: String*): Writer[Task, LogEntry, String] = {
    val cmdLine = (cmd +: args).mkString(" ")
    Log.infoW(s"Executing $cmdLine") ++
      await(Effect.execCmd(cmd, args: _*))(emitCmdResult)
  }

  def execWkHtmlToPdf(input: String, output: Path): Writer[Task, LogEntry, Nothing] =
    execCmd("wkhtmltopdf-h2p.sh", input, output.toString).flatMapO { out =>
      val trimmed = out.trim
      runIf(trimmed.nonEmpty)(Log.infoW(trimmed))
    }

  def readFile(path: Path): Writer[Task, LogEntry, ByteVector] = {
    val bufferSize = 8192
    Log.infoW(s"Reading file ${path.toString}") ++
      liftW(constant(bufferSize).through(nio.file.chunkR(path)))
  }

  def tempFile(prefix: String, suffix: String): Writer[Task, LogEntry, Path] =
    await(Effect.createTempFile(prefix, suffix)) { path =>
      val msg = s"Created temporary file ${path.toString}"
      Log.infoW(msg) ++ emitO(path).onComplete(ignoreO(deleteFile(path)))
    }
}
