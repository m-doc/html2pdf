package html2pdf

import org.http4s.MediaType._
import org.http4s.dsl._
import org.http4s.headers._
import org.http4s.server.HttpService
import org.http4s.server.blaze.BlazeBuilder

object Html2PdfService extends App {
  val route = HttpService {
    case req @ GET -> Root / "pdf" =>
      val url = req.params.get("url")
      val pdf = url.map(converter.mkPdf)
      val rsp = pdf.map(p => Ok(p).withHeaders(`Content-Type`(`application/pdf`)))
      rsp.getOrElse(BadRequest("url parameter not specified"))
  }

  val server = BlazeBuilder
    .bindHttp(8080)
    .mountService(route)
    .run

  server.awaitShutdown()

  val whitelist = Seq(
    "http://en.wikipedia.org",
    "http://github.com",
    "http://gnu.org",
    "http://google.com",
    "http://http4s.org",
    "http://scala-lang.org",
    "http://www.m-net.de"
  )
}
