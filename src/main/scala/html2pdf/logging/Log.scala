package html2pdf
package logging

import scalaz.stream.Process

object Log {
  def info(msg: String): LogWriter[Nothing, Nothing] =
    emitLog(msg, Info)

  def debug(msg: String): LogWriter[Nothing, Nothing] =
    emitLog(msg, Debug)

  def warn(msg: String): LogWriter[Nothing, Nothing] =
    emitLog(msg, Warn)

  def error(msg: String): LogWriter[Nothing, Nothing] =
    emitLog(msg, Error)

  private def emitLog(msg: String, level: LogLevel): LogWriter[Nothing, Nothing] =
    Process.emitW(LogEntry(msg, level))
}
