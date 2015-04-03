package html2pdf

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder

import scalaz.concurrent.Task

object BlazeServer extends App {
  val httpPort: Task[Int] =
    Effect.getPropertyAsInt("HTTP_PORT").map { port =>
      val defaultPort = 8080
      port.getOrElse(defaultPort)
    }

  val server: Task[Server] =
    httpPort.flatMap { port =>
      BlazeBuilder
        .bindHttp(port)
        .mountService(Service.route)
        .start
    }

  server.run.awaitShutdown()
}
