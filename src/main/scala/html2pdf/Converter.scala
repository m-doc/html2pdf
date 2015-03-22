package html2pdf

import java.nio.file.{ Files, Path, Paths }

import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object Converter {
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

  //

  def mkPdf2(url: String): Writer[Task, LogEntry[String], ByteVector] = {
    val bufferSize = 4096
    mkTempFile2("pdf").flatMapO { pdfFile =>
      val makePdf = execWkHtmlToPdf2(url, pdfFile).flatMapO(_ => halt)
      val readPdf = liftW(constant(bufferSize).toSource.through(nio.file.chunkR(pdfFile)))
      val deletePdf = delete2(pdfFile).flatMapO(_ => halt)

      (makePdf ++ readPdf).onComplete(deletePdf)
    }
  }

  def delete2(file: Path): Writer[Task, LogEntry[String], Unit] =
    emitW(LogInfo(s"Deleting ${file.toString}")).toSource ++
      evalO(Task.delay(Files.delete(file)))

  def exec2(cmd: String, args: String*): Writer[Task, LogEntry[String], String] = {
    val cmdLine = cmd +: args
    emitW(LogInfo(s"Running ${cmdLine.mkString(" ")}")).toSource ++
      evalO(Task.delay {
        import scala.sys.process._
        // TODO: do not ignore errors
        cmdLine.!!
      })
  }

  def execWkHtmlToPdf2(input: String, output: Path): Writer[Task, LogEntry[String], String] =
    exec2("wkhtmltopdf-h2p.sh", input, output.toString)

  def mkTempFile2(extension: String): Writer[Task, LogEntry[String], Path] =
    exec2("tempfile", "-p", "h2p", "-s", s".$extension")
      .mapO(filename => Paths.get(filename.trim))

  //

  def evalO[F[_], O](f: F[O]): Writer[F, Nothing, O] =
    liftW(eval(f))
}
