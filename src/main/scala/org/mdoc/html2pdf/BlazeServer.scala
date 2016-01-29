package org.mdoc.html2pdf

import eu.timepit.properly.Property
import eu.timepit.properly.Property.PropertySyntax
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scalaz.concurrent.Task

object BlazeServer extends App {
  val httpPort: Task[Int] = {
    val defaultPort = 8080
    Property.getAsIntOrElse("HTTP_PORT", defaultPort).runTask
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
