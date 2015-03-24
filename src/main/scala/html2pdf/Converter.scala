package html2pdf

import java.nio.file.Path

import html2pdf.LoggedEffect._
import html2pdf.StreamUtil._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream.Writer

object Converter {
  def createPdf(url: String): Writer[Task, LogEntry, ByteVector] =
    Log.infoW(s"Converting $url") ++
      LoggedEffect.tempFile("h2p-", ".pdf").flatMapO { pdf =>
        execWkHtmlToPdf(url, pdf) ++ liftW(readFile(pdf))
      }

  def execWkHtmlToPdf(input: String, output: Path): Writer[Task, LogEntry, Nothing] =
    execCmd("wkhtmltopdf-h2p.sh", input, output.toString)
      .flatMapO(Log.infoW)
}
