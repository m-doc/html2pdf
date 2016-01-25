package html2pdf

import org.scalacheck.Prop._
import org.scalacheck.Properties

class EffectSpec extends Properties("Effect") {

  property("execCmd") = secure {
    val result = Effect.execCmd("echo", "hello").run
    result.out.trim == "hello"
  }
}
