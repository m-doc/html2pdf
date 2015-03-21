package html2pdf

import java.nio.file.Paths

import org.http4s.TransferCoding
import org.http4s.dsl._
import org.http4s.headers.{ `Transfer-Encoding`, `Content-Type` }
import org.http4s.server.HttpService
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.MediaType._
import scalaz.concurrent.Task
import scalaz.stream.Process
import scalaz.stream.nio

object Html2PdfService extends App {
  val htmlfile = "/home/frank/test.html"
  val pdffile = "/home/frank/test.pdf"

  val route = HttpService {
    case req @ GET -> Root => Ok("Hello World!")
    case req @ GET -> Root / "pdf" => Ok(xxx).withHeaders(`Content-Type`(`application/pdf`))
    case req @ GET -> Root / "pdf2" => Ok(io.exec("cat", "/home/frank/test.pdf").run).withHeaders(`Content-Type`(`application/pdf`))
    case req @ GET -> Root / "test.pdf" => Ok(Process.constant(1024).through(scalaz.stream.io.fileChunkR(pdffile)))
      .withHeaders(`Content-Type`(`application/pdf`))
    case req @ GET -> Root / "html" => Ok(scalaz.stream.io.linesR(htmlfile))
      //.withHeaders(`Content-Type`(`application/pdf`), `Transfer-Encoding`(TransferCoding.chunked))
      .withHeaders(`Content-Type`(`text/html`))

  }

  val server = BlazeBuilder
    .bindLocal(8080)
    .mountService(route)
    .run

  server.awaitShutdown()

  def xxx: Process[Task, String] = {
    val outFile = "/home/frank/test.pdf"
    val html =
      """
        |<html>
        |  <body><h1>Hello World</h1</body>
        |</html>
      """.stripMargin

    Process.eval(io.mkTempFile).flatMap { inFile =>
      val write = Process(html).toSource
        .pipe(scalaz.stream.text.utf8Encode)
        .to(nio.file.chunkW(inFile))

      val exec = Process.eval_(io.exec("wkhtmltopdf", inFile.toString, outFile))
      val read = Process("hallo ").toSource ++ scalaz.stream.io.linesR(outFile) //nio.file.linesR(Paths.get(outFile))

      val p = Process("<pre>").toSource ++ write.drain ++ exec.drain ++ read ++ Process("</pre>").toSource
      p.observe(scalaz.stream.io.stdOutLines)

      read.observe(scalaz.stream.io.stdOutLines)
    }

    val c = scalaz.stream.io.linesR(outFile).runLog.run

    Process("<pre>", "Welt").toSource ++ Process.emitAll(c).toSource ++ Process("hallo", "Welt").toSource ++ scalaz.stream.io.linesR(outFile)
  }

}
