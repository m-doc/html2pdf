package html2pdf

import scalaz.concurrent.Task
import scalaz.stream.Process._
import scalaz.stream._

object StreamUtil {
  def evalO[F[_], O](f: F[O]): Writer[F, Nothing, O] =
    liftW(eval(f))

  def ignoreO[F[_], W, O](p: Writer[F, W, O]): Writer[F, W, Nothing] =
    p.flatMapO(_ => halt)

  def sourceW[W](w: W): Writer[Task, W, Nothing] =
    emitW(w).toSource
}
