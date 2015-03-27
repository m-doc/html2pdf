package html2pdf

import html2pdf.StreamUtil._
import html2pdf.logging._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process.emitW
import scalaz.stream._

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
    io.stdOutLines.contramapEval(_.format)

  def fileSink(f: String): Sink[Task, LogEntry] =
    nio.file.chunkW(f).contramapEval(_.format.map(s => ByteVector.view(s.getBytes)))
}
