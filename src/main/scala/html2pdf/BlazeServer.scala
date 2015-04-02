package html2pdf

import org.http4s.server.blaze.BlazeBuilder

object BlazeServer extends App {
  val httpPort = Effect.getProperty("HTTP_PORT").map { prop =>
    val default = 8080
    prop.map(_.fold(default)(_.toInt))
  }

  val server = httpPort.flatMap { port =>
    BlazeBuilder
      .bindHttp(port)
      .mountService(Service.route)
      .start
  }

  server.run.awaitShutdown()
}
