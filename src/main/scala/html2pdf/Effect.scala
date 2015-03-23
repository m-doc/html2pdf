package html2pdf

import java.nio.file.{ Files, Path }

import scalaz.concurrent.Task

object Effect {
  def deleteFile(path: Path): Task[Unit] =
    Task.delay(Files.delete(path))

  case class CmdResult(cmd: String, out: String, err: String, status: Int)

  def execCmd(cmd: String, args: String*): Task[CmdResult] =
    Task.delay {
      import scala.sys.process._

      def append(sb: StringBuilder)(s: String) = {
        sb.append(s).append(System.lineSeparator()); ()
      }

      val outBuf = new StringBuilder
      val errBuf = new StringBuilder
      val logger = ProcessLogger(append(outBuf), append(errBuf))

      val cmdLine = cmd +: args
      val status = cmdLine.!(logger)
      CmdResult(cmdLine.mkString(" "), outBuf.result(), errBuf.result(), status)
    }
}
