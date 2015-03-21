package html2pdf

import java.nio.file.{ Files, Path, Paths }

import scalaz.concurrent.Task

object io {
  def delete(path: Path): Task[Unit] =
    Task.delay {
      Files.delete(path)
    }

  def exec(cmd: String, args: String*): Task[String] =
    Task.delay {
      import scala.sys.process._
      s"$cmd ${args.mkString(" ")}".!!
    }

  def mkTempFile: Task[Path] =
    exec("tempfile", "-p", "h2pms", "-s", ".html")
      .map(filename => Paths.get(filename.trim))
}
