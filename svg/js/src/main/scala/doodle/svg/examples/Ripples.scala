/*
 * Copyright 2019 Noel Welsh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package doodle
package svg
package examples

object Ripples {
  import cats.effect.IO
  import cats.instances.all._
  import cats.syntax.all._
  import doodle.core._
  import doodle.syntax._
  import doodle.svg.effect._
  import monix.reactive.Observable
  import monix.catnap.{ConcurrentQueue,SchedulerEffect}
  import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

  final case class Ripple(age: Int, x: Double, y: Double) {
    val maxAge = 200
    def alive: Boolean = age <= maxAge

    def older: Ripple =
      this.copy(age = age + 1)

    def picture: Picture[Unit] =
      Picture { implicit algebra =>
        algebra
          .circle(age.toDouble)
          .strokeColor(Color.hotpink.alpha(((maxAge - age) / (maxAge.toDouble)).normalized))
          .at(x, y)
      }
  }

  def ripples(canvas: Canvas): IO[Observable[Picture[Unit]]] = {
    implicit val cs = SchedulerEffect.contextShift[IO](svgScheduler)

    ConcurrentQueue[IO]
      .bounded[Option[Ripple]](5)
      .map{ queue =>
        canvas
          .redraw
          .map(_ => none[Ripple])
          .mapEvalF(r => queue.offer(r))
          .subscribe()

        canvas
          .mouseMove
          .throttleFirst(FiniteDuration(100, MILLISECONDS)) // Stop spamming with too many mouse events
          .map(pt => Ripple(0, pt.x, pt.y).some)
          .mapEvalF(r => queue.offer(r))
          .subscribe()

        Observable
          .repeatEvalF(queue.poll)
          .scan(List.empty[Ripple]){(ripples, ripple) =>
            ripple match {
              case Some(r) => r :: ripples
              case None => ripples.filter(_.alive).map(_.older)
            }
          }
          .map(ripples => Picture{ implicit algebra =>
                 ripples.map(_.picture(algebra)).allOn
               })
      }
  }
}
