package html2pdf

import java.nio.file.{ Path, Paths }

import html2pdf.StreamUtil._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object Converter {
  def mkPdf2(url: String): Writer[Task, LogEntry[String], ByteVector] = {
    val bufferSize = 4096
    mkTempFile("pdf").flatMapO { pdfFile =>
      val makePdf = execWkHtmlToPdf(url, pdfFile).flatMapO(_ => halt) // we should log the output of wkhtmltopdf instead of drainig it
      val readPdf = liftW(constant(bufferSize).toSource.through(nio.file.chunkR(pdfFile)))
      val deletePdf = ignoreO(delete(pdfFile))

      (makePdf ++ readPdf).onComplete(deletePdf)
    }
  }

  def delete(file: Path): Writer[Task, LogEntry[String], Unit] =
    sourceW(LogInfo(s"Deleting ${file.toString}")) ++
      evalO(Effect.deleteFile(file))

  def exec(cmd: String, args: String*): Writer[Task, LogEntry[String], String] = {
    val cmdLine = cmd +: args
    sourceW(LogInfo(s"Running ${cmdLine.mkString(" ")}")) ++
      evalO(Task.delay {
        import scala.sys.process._
        // TODO: do not ignore errors
        cmdLine.!!
      })
  }

  def execWkHtmlToPdf(input: String, output: Path): Writer[Task, LogEntry[String], String] =
    exec("wkhtmltopdf-h2p.sh", input, output.toString)

  def mkTempFile(extension: String): Writer[Task, LogEntry[String], Path] =
    exec("tempfile", "-p", "h2p", "-s", s".$extension")
      .mapO(filename => Paths.get(filename.trim))
}
