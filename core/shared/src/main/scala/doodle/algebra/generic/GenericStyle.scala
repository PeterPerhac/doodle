/*
 * Copyright 2015 Creative Scala
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
package algebra
package generic

import doodle.core.{Cap, Color, Join}

trait GenericStyle[F[_]] extends Style[Finalized[F, ?]] {
  def fillColor[A](image: Finalized[F, A], fillColor: Color): Finalized[F, A] =
    Finalized.contextTransform(_.fillColor(fillColor))(image)

  def strokeColor[A](image: Finalized[F, A],
                     strokeColor: Color): Finalized[F, A] =
    Finalized.contextTransform(_.strokeColor(strokeColor))(image)

  def strokeWidth[A](image: Finalized[F, A],
                     strokeWidth: Double): Finalized[F, A] =
    Finalized.contextTransform(_.strokeWidth(strokeWidth))(image)

  def strokeCap[A](image: Finalized[F, A], cap: Cap): Finalized[F, A] =
    Finalized.contextTransform(_.strokeCap(cap))(image)

  def strokeJoin[A](image: Finalized[F, A], join: Join): Finalized[F, A] =
    Finalized.contextTransform(_.strokeJoin(join))(image)

  def noFill[A](image: Finalized[F, A]): Finalized[F, A] =
    Finalized.contextTransform(_.noFill)(image)

  def noStroke[A](image: Finalized[F, A]): Finalized[F, A] =
    Finalized.contextTransform(_.noStroke)(image)
}
