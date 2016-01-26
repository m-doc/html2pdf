package html2pdf

import org.scalacheck.Prop._
import org.scalacheck.Properties

class WriterEffectSpec extends Properties("WritterEffect") {

  property("execCmd") = secure {
    WriterEffect.execCmd("echo", "hello").stripW.runLog.run.map(_.trim) == Vector("hello")
  }
}
