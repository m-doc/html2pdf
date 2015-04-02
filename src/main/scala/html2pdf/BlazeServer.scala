package html2pdf

import org.http4s.server.blaze.BlazeBuilder

object BlazeServer extends App {
  val httpPort = Effect.getProperty("HTTP_PORT").map { prop =>
    val defaultPort = 8080
    prop.fold(defaultPort)(_.toInt)
  }

  val server = httpPort.flatMap { port =>
    BlazeBuilder
      .bindHttp(port)
      .mountService(Service.route)
      .start
  }

  server.run.awaitShutdown()
}
