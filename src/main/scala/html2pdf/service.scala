package html2pdf

import org.http4s.MediaType._
import org.http4s.dsl._
import org.http4s.headers._
import org.http4s.server.HttpService
import org.http4s.server.blaze.BlazeBuilder

object service extends App {
  val whitelist = Seq(
    "http://en.wikipedia.org",
    "http://github.com",
    "http://gnu.org",
    "http://google.com",
    "http://http4s.org",
    "http://m-net.de",
    "http://reddit.com",
    "http://scala-lang.org"
  )

  val route = HttpService {
    case req @ GET -> Root / "pdf" =>
      val param = "url"
      val response = req.params.get(param).map { url =>
        if (whitelist.contains(url))
          Ok(converter.mkPdf(url))
            .withHeaders(`Content-Type`(`application/pdf`))
        else
          BadRequest(s"URL '$url' is not in the whitelist")
      }
      response.getOrElse(BadRequest(s"parameter '$param' is not specified"))
  }

  val server = BlazeBuilder
    .bindHttp(8080)
    .mountService(route)
    .run

  server.awaitShutdown()
}
