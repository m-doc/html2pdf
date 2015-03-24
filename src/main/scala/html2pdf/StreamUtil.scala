package html2pdf

import java.nio.file.Path

import scodec.bits.ByteVector

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object StreamUtil {
  def evalO[F[_], O](f: F[O]): Writer[F, Nothing, O] =
    liftW(eval(f))

  def ignoreO[F[_], W, O](p: Writer[F, W, O]): Writer[F, W, Nothing] =
    p.flatMapO(_ => halt)

  def readFile(path: Path): Process[Task, ByteVector] = {
    val bufferSize = 8192
    constant(bufferSize).through(nio.file.chunkR(path))
  }

  def runIf[F[_], O](b: Boolean)(p: => Process[F, O]): Process[F, O] =
    if (b) p else halt
}
