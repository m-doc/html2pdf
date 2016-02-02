package org.mdoc.html2pdf

import org.mdoc.fshell.Shell
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scalaz.NonEmptyList

class WriterEffectSpec extends Properties("WritterEffect") {

  property("execCmd") = secure {
    WriterEffect.execCmd(Shell.readProcess(NonEmptyList("echo", "hello"))).stripW.runLog.run.map(_.trim) == Vector("hello")
  }
}
