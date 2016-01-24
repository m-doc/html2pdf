package html2pdf

import java.nio.file.Files
import org.scalacheck.Prop._
import org.scalacheck.Properties

class EffectSpec extends Properties("Effect") {

  property("createTempFile") = secure {
    val path = Effect.createTempFile("", "").run
    val created = Files.exists(path)
    Files.delete(path)
    created
  }
}
