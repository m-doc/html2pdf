package html2pdf

import scalaz.stream.Process._
import scalaz.stream._

object StreamUtil {
  def evalO[F[_], O](f: F[O]): Writer[F, Nothing, O] =
    await(f)(emitO)

  def ignoreO[F[_], W, O](p: Writer[F, W, O]): Writer[F, W, Nothing] =
    p.flatMapO(_ => halt)

  def runIf[F[_], O](b: Boolean)(p: => Process[F, O]): Process[F, O] =
    if (b) p else halt
}
