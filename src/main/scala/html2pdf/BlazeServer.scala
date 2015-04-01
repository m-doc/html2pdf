package html2pdf

import org.http4s.server.blaze.BlazeBuilder

object BlazeServer extends App {
  // use Task
  val port = Option(System.getProperty("HTTP_PORT"))
    .map(_.toInt).getOrElse(8080)

  BlazeBuilder
    .bindHttp(port)
    .mountService(Service.route)
    .run
    .awaitShutdown()
}
