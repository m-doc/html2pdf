package html2pdf
package logging

import scalaz.stream.Process

object Log {
  def info(msg: String): LogWriter[Nothing, Nothing] =
    Process.emitW(LogEntry(msg, Info))

  def debug(msg: String): LogWriter[Nothing, Nothing] =
    Process.emitW(LogEntry(msg, Debug))

  def warn(msg: String): LogWriter[Nothing, Nothing] =
    Process.emitW(LogEntry(msg, Warn))

  def error(msg: String): LogWriter[Nothing, Nothing] =
    Process.emitW(LogEntry(msg, Error))
}
