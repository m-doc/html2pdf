package html2pdf

import java.nio.file.Paths

import html2pdf.BuildInfo._
import html2pdf.logging.LogEntry
import html2pdf.logging.LogSink._
import org.http4s.MediaType._
import org.http4s.Uri
import org.http4s.dsl._
import org.http4s.headers._
import org.http4s.server.HttpService
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process

object Service {
  def pdfSource(url: String, remoteAddr: Option[String]): Process[Task, ByteVector] = {
    val logFile = Paths.get(s"logs/$name.log")
    (remoteAddr.fold(Process.halt: scalaz.stream.Writer[Nothing, LogEntry, Nothing])(r => logging.Log.info(s"from $r")) ++
      WriterEffect.createPdf(url)).drainW(stdoutAndFileSink(logFile))
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
    case GET -> Root =>
      Uri.fromString(homepage).fold(_ => Ok(name), TemporaryRedirect(_))

    case req @ GET -> Root / "pdf" =>
      println(req)
      println(req.uri.renderString)
      println(req.method.name)
      val param = "url"
      val response = req.params.get(param).map { url =>
        if (whitelist.contains(url))
          Ok(pdfSource(url, req.remoteAddr))
            .withHeaders(`Content-Type`(`application/pdf`))
        else
          BadRequest(s"URL '$url' is not in the whitelist")
      }
      response.getOrElse(BadRequest(s"parameter '$param' is not specified"))
  }
}
