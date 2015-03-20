package html2pdf

import org.http4s.dsl._
import org.http4s.server.HttpService
import org.http4s.server.blaze.BlazeBuilder

object Html2PdfService extends App {
  val route = HttpService {
    case req @ GET -> Root => Ok("Hello World!")
  }

  val server = BlazeBuilder
    .bindLocal(8080)
    .mountService(route)
    .run

  server.awaitShutdown()
}
