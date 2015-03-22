package html2pdf

import org.http4s.server.blaze.BlazeBuilder

object BlazeServer extends App {
  BlazeBuilder
    .bindHttp(8080)
    .mountService(Service.route)
    .run
    .awaitShutdown()
}
