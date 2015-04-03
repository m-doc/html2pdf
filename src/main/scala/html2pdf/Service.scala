package html2pdf

import java.nio.file.Paths

import html2pdf.BuildInfo._
import html2pdf.logging.LogSink._
import org.http4s.MediaType._
import org.http4s.dsl._
import org.http4s.headers._
import org.http4s.server.HttpService
import org.http4s.{ Request, Uri }
import scodec.bits.ByteVector

import scalaz.\/
import scalaz.concurrent.Task
import scalaz.stream.Process
import scalaz.syntax.either._

object Service {
  def extractUrl(req: Request): String \/ String = {
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

    val key = "url"
    req.params.get(key)
      .fold(s"parameter $key is not specified".left[String])(_.right[String])
      .flatMap { url =>
        if (whitelist.contains(url)) url.right
        else s"URL '$url' is not in the whitelist".left
      }
  }

  def pdfSource(url: String): Process[Task, ByteVector] = {
    val logFile = Paths.get(s"logs/$name.log")
    WriterEffect.createPdf(url).drainW(stdoutAndFileSink(logFile))
  }

  val route = HttpService {
    case GET -> Root =>
      Uri.fromString(homepage).fold(_ => Ok(name), TemporaryRedirect(_))

    case req @ GET -> Root / "pdf" =>
      extractUrl(req).fold(BadRequest(_),
        url => Ok(pdfSource(url)).withHeaders(`Content-Type`(`application/pdf`)))
  }
}
