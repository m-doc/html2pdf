package html2pdf

import java.nio.file.{ Files, Path }

import scalaz.concurrent.Task

object Effect {
  def deleteFile(path: Path): Task[Unit] =
    Task.delay(Files.delete(path))

  case class CmdOutput(out: String, err: String, status: Int)

  def execCmd(cmd: String, args: String*): Task[CmdOutput] =
    Task.delay {
      import scala.sys.process._

      val outBuf = new StringBuilder
      val errBuf = new StringBuilder
      val logger = ProcessLogger(
        s => { outBuf.append(s).append("\n"); () },
        s => { errBuf.append(s).append("\n"); () })

      val status = (cmd +: args).!(logger)
      CmdOutput(outBuf.result(), errBuf.result(), status)
    }
}
