package html2pdf

import java.nio.file.Path

import html2pdf.LoggedEffect._
import html2pdf.StreamUtil._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object Converter {
  def createPdf(url: String): Writer[Task, LogEntry, ByteVector] = {
    Log.infoW(s"Converting $url") ++
      LoggedEffect.createTempFile("h2p-", ".pdf").flatMapO { pdf =>
        val make = execWkHtmlToPdf(url, pdf)
        val read = liftW(readFile(pdf))
        val delete = ignoreO(LoggedEffect.deleteFile(pdf))
        (make ++ read).onComplete(delete)
      }
  }

  def execWkHtmlToPdf(input: String, output: Path): Writer[Task, LogEntry, Nothing] =
    execCmd("wkhtmltopdf-h2p.sh", input, output.toString)
      .flatMapO(Log.infoW)

  def readFile(path: Path): Process[Task, ByteVector] = {
    val bufferSize = 8192
    constant(bufferSize).toSource.through(nio.file.chunkR(path))
  }
}
