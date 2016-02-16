package org.mdoc.html2pdf

import com.typesafe.scalalogging.StrictLogging
import eu.timepit.properly.Property
import eu.timepit.properly.Property.PropertySyntax
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeBuilder
import scala.util.Try
import scalaz.concurrent.Task

object BlazeServer extends App with StrictLogging {
  val httpPort: Task[Int] = {
    val defaultPort = 8080
    Property.getAsIntOrElse("HTTP_PORT", defaultPort).runTask
  }

  val server: Task[Server] =
    httpPort.flatMap { port =>
      BlazeBuilder
        .bindHttp(port, "::")
        .mountService(Service.route)
        .start
    }

  Try(server.run.awaitShutdown()).recover {
    case throwable =>
      logger.error("awaitShutdown()", throwable)
      sys.exit(1)
  }
}
