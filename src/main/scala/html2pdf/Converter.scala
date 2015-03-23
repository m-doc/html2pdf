package html2pdf

import java.nio.file.{Path, Paths}

import html2pdf.LoggedEffect._
import html2pdf.StreamUtil._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object Converter {
  def mkPdf(url: String): Writer[Task, LogEntry, ByteVector] = {
    val bufferSize = 8192
    Log.infoW(s"Converting $url") ++ mkTempFile("pdf").flatMapO { pdf =>
      val make = execWkHtmlToPdf(url, pdf)
      val read = liftW(constant(bufferSize).toSource.through(nio.file.chunkR(pdf)))
      val delete = ignoreO(LoggedEffect.deleteFile(pdf))
      (make ++ read).onComplete(delete)
    }
  }

  def execWkHtmlToPdf(input: String, output: Path): Writer[Task, LogEntry, Nothing] =
    execCmd("wkhtmltopdf-h2p.sh", input, output.toString)
      .flatMapO(Log.infoW)

  def mkTempFile(extension: String): Writer[Task, LogEntry, Path] =
    execCmd("tempfile", "-p", "h2p", "-s", s".$extension")
      .mapO(filename => Paths.get(filename.trim))
}
