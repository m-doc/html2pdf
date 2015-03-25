package html2pdf

import org.http4s.MediaType._
import org.http4s.Uri
import org.http4s.dsl._
import org.http4s.headers._
import org.http4s.server.HttpService

object Service {
  val rootResponse = {
    val redirect = for {
      url <- BuildInfo.homepage
      uri <- Uri.fromString(url.toString).toOption
    } yield TemporaryRedirect(uri)
    redirect.getOrElse(Ok(BuildInfo.name))
  }

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
    case GET -> Root => rootResponse
    case req @ GET -> Root / "pdf" =>
      val param = "url"
      val response = req.params.get(param).map { url =>
        if (whitelist.contains(url))
          Ok(WriterEffect.createPdf(url).drainW(Log.stdoutSink))
            .withHeaders(`Content-Type`(`application/pdf`))
        else
          BadRequest(s"URL '$url' is not in the whitelist")
      }
      response.getOrElse(BadRequest(s"parameter '$param' is not specified"))
  }
}
