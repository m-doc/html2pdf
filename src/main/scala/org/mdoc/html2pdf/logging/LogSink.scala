package org.mdoc.html2pdf
package logging

import java.nio.file.Path
import org.mdoc.fshell.Shell
import org.mdoc.fshell.Shell.ShellSyntax
import org.mdoc.html2pdf.StreamUtil._
import scalaz.concurrent.Task
import scalaz.stream._
import scalaz.stream.Process._
import scodec.bits.ByteVector

object LogSink {
  def stdoutAndFileSink(path: Path): Sink[Task, LogEntry] =
    stdoutSink.combine(fileSink(path))

  def fileSink(path: Path): Sink[Task, LogEntry] = {
    eval(Shell.createParentDirectories(path).runTask.handle { case _ => () }).flatMap { _ =>
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
