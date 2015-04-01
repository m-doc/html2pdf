package html2pdf
package logging

import java.nio.file.Path

import html2pdf.StreamUtil._
import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object LogSink {
  def stdoutAndFileSink(path: Path): Sink[Task, LogEntry] =
    stdoutSink.combine(fileSink(path))

  def fileSink(path: Path): Sink[Task, LogEntry] = {
    eval(Effect.createParentDirectories(path)).flatMap { _ =>
      io.fileChunkW(path.toString, bufferSize, append = true).compose {
        _.format.map { msg =>
          val line = msg + System.lineSeparator()
          ByteVector.view(line.getBytes("UTF-8"))
        }
      }
    }
  }

  def stdoutSink: Sink[Task, LogEntry] =
    io.stdOutLines.compose(_.format)
}
