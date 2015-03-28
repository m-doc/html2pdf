package html2pdf.logging

import java.util.Date

import scalaz.concurrent.Task
import scalaz.stream.Process.emitW
import scalaz.stream._

case class LogEntry(msg: String, level: LogLevel) {
  def format: Task[String] =
    Task.delay {
      val now = (new Date).toString
      s"[$now] ${level.toUpperCase}: $msg"
    }
}

object LogEntry {
  def infoW(msg: String): Writer[Nothing, LogEntry, Nothing] =
    emitW(LogEntry(msg, Info))

  def debugW(msg: String): Writer[Nothing, LogEntry, Nothing] =
    emitW(LogEntry(msg, Debug))

  def warnW(msg: String): Writer[Nothing, LogEntry, Nothing] =
    emitW(LogEntry(msg, Warn))

  def errorW(msg: String): Writer[Nothing, LogEntry, Nothing] =
    emitW(LogEntry(msg, Error))
}
