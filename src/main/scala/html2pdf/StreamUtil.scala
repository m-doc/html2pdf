package html2pdf

import scalaz.Bind
import scalaz.stream.Process._
import scalaz.stream._
import scalaz.syntax.bind._

object StreamUtil {
  implicit class MyChannelSyntax[F[_], I, O](val self: Channel[F, I, O]) extends AnyVal {
    def contramapEval[I0](f: I0 => F[I])(implicit F: Bind[F]): Channel[F, I0, O] =
      self.map(g => f.andThen(_.flatMap(g)))
  }

  implicit class MyWriterSyntax[F[_], W, O](val self: Writer[F, W, O]) extends AnyVal {
    def ignoreO: Writer[F, W, Nothing] =
      self.flatMapO(_ => halt)
  }

  def evalO[F[_], O](f: F[O]): Writer[F, Nothing, O] =
    await(f)(emitO)

  def runIf[F[_], O](b: Boolean)(p: => Process[F, O]): Process[F, O] =
    if (b) p else halt
}
