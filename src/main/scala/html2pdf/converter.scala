package html2pdf

import java.nio.file.{ Files, Path, Paths }

import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object converter {
  def mkPdf(url: String): Process[Task, ByteVector] = {
    val bufferSize = 4096
    eval(mkTempFile("pdf")).flatMap { pdfFile =>
      val makePdf = eval_(execWkHtmlToPdf(url, pdfFile))
      val readPdf = constant(bufferSize).toSource.through(nio.file.chunkR(pdfFile))
      val deletePdf = eval_(delete(pdfFile))

      (makePdf ++ readPdf).onComplete(deletePdf)
    }
  }

  def delete(path: Path): Task[Unit] =
    Task.delay(Files.delete(path))

  def exec(cmd: String, args: String*): Task[String] =
    Task.delay {
      import scala.sys.process._
      (cmd +: args).!!
    }

  def execWkHtmlToPdf(input: String, output: Path): Task[String] =
    exec("wkhtmltopdf-h2p.sh", input, output.toString)

  def mkTempFile(extension: String): Task[Path] =
    exec("tempfile", "-p", "h2p", "-s", s".$extension")
      .map(filename => Paths.get(filename.trim))
}
