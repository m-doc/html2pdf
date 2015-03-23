package html2pdf

import java.util.Date
import scalaz.concurrent.Task
import scalaz.stream.Process.emitW
import scalaz.stream._

sealed trait LogLevel {
  def toUpperCase: String =
    this match {
      case Info => "INFO"
      case Debug => "DEBUG"
      case Warn => "WARN"
      case Error => "ERROR"
    }
}

case object Info extends LogLevel
case object Debug extends LogLevel
case object Warn extends LogLevel
case object Error extends LogLevel

case class LogEntry(entry: String, level: LogLevel)

object Log {
  def info(msg: String): LogEntry = LogEntry(msg, Info)
  def debug(msg: String): LogEntry = LogEntry(msg, Debug)
  def warn(msg: String): LogEntry = LogEntry(msg, Warn)
  def error(msg: String): LogEntry = LogEntry(msg, Error)

  def infoW(msg: String): Writer[Nothing, LogEntry, Nothing] = emitW(info(msg))
  def debugW(msg: String): Writer[Nothing, LogEntry, Nothing] = emitW(debug(msg))
  def warnW(msg: String): Writer[Nothing, LogEntry, Nothing] = emitW(warn(msg))
  def errorW(msg: String): Writer[Nothing, LogEntry, Nothing] = emitW(error(msg))

  def stdoutSink: Sink[Task, LogEntry] =
    io.stdOutLines.contramap { log: LogEntry =>
      val ts = new Date().toString
      s"[$ts] ${log.level.toUpperCase}: ${log.entry}"
    }
}
