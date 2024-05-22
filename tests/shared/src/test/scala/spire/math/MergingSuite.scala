/*
 * **********************************************************************\
 * * Project                                                              **
 * *       ______  ______   __    ______    ____                          **
 * *      / ____/ / __  /  / /   / __  /   / __/     (c) 2011-2021        **
 * *     / /__   / /_/ /  / /   / /_/ /   / /_                            **
 * *    /___  / / ____/  / /   / __  /   / __/   Erik Osheim, Tom Switzer **
 * *   ____/ / / /      / /   / / | |   / /__                             **
 * *  /_____/ /_/      /_/   /_/  |_|  /____/     All rights reserved.    **
 * *                                                                      **
 * *      Redistribution and use permitted under the MIT license.         **
 * *                                                                      **
 * \***********************************************************************
 */

package spire
package math

import spire.implicits._
import spire.algebra.Order

class CountingOrder[T: Order] extends Order[T] {
  val wrapped = implicitly[Order[T]]
  var count = 0

  override def compare(x: T, y: T): Int = {
    count += 1
    wrapped.compare(x, y)
  }
}

class MergingSuite extends munit.FunSuite {

  test("binary merge") {
    val a = Array.range(0, 100).map(_ * 2)
    val b = Array.range(1, 100).map(_ * 2)
    val o = new CountingOrder[Int]
    val r = BinaryMerge.merge(a, b)(using o, ClassTag.Int)
    assert(r.sorted.corresponds(r)(_ == _))
    assert(o.count < 200)
  }

  test("linear merge") {
    val a = Array.range(0, 100).map(_ * 2)
    val b = Array.range(1, 100).map(_ * 3)
    val o = new CountingOrder[Int]
    val r1 = LinearMerge.merge(a, b)(using o, ClassTag.Int)
    val r2 = LinearMerge.merge(b, a)(using o, ClassTag.Int)
    assert(r1.sorted.corresponds(r1)(_ == _))
    assert(r2.sorted.corresponds(r2)(_ == _))
  }
}
