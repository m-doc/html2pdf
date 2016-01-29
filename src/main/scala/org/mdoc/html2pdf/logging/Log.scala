package org.mdoc.html2pdf
package logging

import scalaz.stream.Process

object Log {
  def info(msg: String): LogWriter[Nothing, Nothing] =
    emitL(msg, Info)

  def debug(msg: String): LogWriter[Nothing, Nothing] =
    emitL(msg, Debug)

  def warn(msg: String): LogWriter[Nothing, Nothing] =
    emitL(msg, Warn)

  def error(msg: String): LogWriter[Nothing, Nothing] =
    emitL(msg, Error)

  private def emitL(msg: String, level: LogLevel): LogWriter[Nothing, Nothing] =
    Process.emitW(LogEntry(msg, level))
}
