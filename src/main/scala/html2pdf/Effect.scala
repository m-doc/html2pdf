package html2pdf

import java.nio.file.{ Files, Path }
import scala.sys.process._
import scalaz.concurrent.Task

object Effect {
  def createParentDirectories(path: Path): Task[Path] =
    Task.delay {
      val parent = path.getParent
      if (Files.isDirectory(parent)) { parent }
      else { Files.createDirectories(parent) }
    }

  def createTempFile(prefix: String, suffix: String): Task[Path] =
    Task.delay(Files.createTempFile(prefix, suffix))

  def deleteFile(path: Path): Task[Unit] =
    Task.delay(Files.delete(path))

  case class CmdResult(cmd: String, out: String, err: String, status: Int)

  def execCmd(cmd: String, args: String*): Task[CmdResult] = {
    def append(sb: StringBuilder)(s: String): Unit = {
      sb.append(s).append(System.lineSeparator())
      ()
    }

    Task.delay {
      val outBuf = new StringBuilder
      val errBuf = new StringBuilder
      val logger = ProcessLogger(append(outBuf), append(errBuf))

      val cmdLine = cmd +: args
      val status = cmdLine.!(logger)
      CmdResult(cmdLine.mkString(" "), outBuf.result(), errBuf.result(), status)
    }
  }
}
