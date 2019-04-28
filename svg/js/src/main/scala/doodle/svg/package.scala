package doodle

import cats.data.Writer
import doodle.effect.Renderer
import doodle.algebra.generic.reified.Reified

package object svg {
  type Algebra = doodle.svg.algebra.Algebra.type
  type Drawing[A] = doodle.algebra.generic.Finalized[Writer[List[Reified],?],A]
  type Renderable[A] = doodle.algebra.generic.Renderable[Writer[List[Reified],?],A]

  type Frame = doodle.svg.effect.Frame
  type Canvas = doodle.svg.effect.Canvas
  implicit val svgRenderer: Renderer[Algebra, Drawing, Frame, Canvas] =
    doodle.svg.effect.SvgRenderer

  val Frame = doodle.svg.effect.Frame

  type Image[A] = doodle.algebra.Image[Algebra, Drawing, A]
  object Image {
    def apply(f: Algebra => Drawing[Unit]): Image[Unit] = {
      new Image[Unit] {
        def apply(implicit algebra: Algebra): Drawing[Unit] =
          f(algebra)
      }
    }
  }
}
