/*
 * Copyright 2015 noelwelsh
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
package explore
package effect

import cats.effect.IO
import monix.reactive.Observable

trait Explorer[UI, A] {
  def ui: UI
  def value: Observable[A]
  def render: IO[Observable[A]]
}

trait ExplorerFactory[UI, A] {
  def create: Explorer[UI, A]
}
