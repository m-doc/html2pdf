package html2pdf

import java.nio.file.Path

import html2pdf.StreamUtil._

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream.Writer

object LoggedEffect {
  def createTempFile(prefix: String, suffix: String): Writer[Task, LogEntry, Path] =
    evalO(Effect.createTempFile(prefix, suffix)).flatMapO { path =>
      Log.infoW(s"Created temporary file ${path.toString}") ++ emitO(path)
    }

  def deleteFile(path: Path): Writer[Task, LogEntry, Unit] =
    Log.infoW(s"Deleting ${path.toString}") ++
      evalO(Effect.deleteFile(path))

  def execCmd(cmd: String, args: String*): Writer[Task, LogEntry, String] = {
    val cmdLine = (cmd +: args).mkString(" ")
    Log.infoW(s"Executing $cmdLine") ++
      eval(Effect.execCmd(cmd, args: _*)).flatMap(logCmdResult)
  }

  def logCmdResult(res: Effect.CmdResult): Writer[Nothing, LogEntry, String] = {
    def errLog = Log.warnW(res.err)
    def statusLog = Log.errorW(s"${res.cmd} exited with status ${res.status.toString}")
    emitO(res.out) ++ runIf(res.err.nonEmpty)(errLog) ++ runIf(res.status != 0)(statusLog)
  }

}
