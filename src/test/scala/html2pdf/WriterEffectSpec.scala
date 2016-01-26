package html2pdf

import org.mdoc.fshell.Shell
import org.scalacheck.Prop._
import org.scalacheck.Properties

class WriterEffectSpec extends Properties("WritterEffect") {

  property("execCmd") = secure {
    WriterEffect.execCmd(Shell.readProcess("echo", List("hello"))).stripW.runLog.run.map(_.trim) == Vector("hello")
  }
}
