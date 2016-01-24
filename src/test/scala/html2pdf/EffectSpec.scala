package html2pdf

import org.scalacheck.Prop._
import org.scalacheck.Properties

class EffectSpec extends Properties("Effect") {

  property("getPropertyAsInt") = secure {
    scala.util.Properties.setProp("someInt", "42")
    Effect.getPropertyAsInt("someInt").run.contains(42)
  }
}
