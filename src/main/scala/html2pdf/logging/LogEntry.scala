package html2pdf.logging

import java.util.Date

import scalaz.concurrent.Task

case class LogEntry(msg: String, level: LogLevel) {
  def format: Task[String] =
    Task.delay {
      val now = (new Date).toString
      s"[$now] ${level.toUpperCase}: $msg"
    }
}
