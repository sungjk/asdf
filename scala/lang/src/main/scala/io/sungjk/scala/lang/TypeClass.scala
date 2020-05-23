package io.sungjk.scala.lang

object TypeClass {
  case class Point(x: Int, y: Int)

  trait Adder[A] {
    def zero: A
    def add(x: A, y: A): A
  }

  def sum[A](xs: List[A])(adder: Adder[A]): A =
    xs.foldLeft(adder.zero)(adder.add)

  val intAdder = new Adder[Int] {
    override def zero: Int = 0
    override def add(x: Int, y: Int): Int = x + y
  }
  val pointAdder = new Adder[Point] {
    override def zero: Point = Point(0, 0)
    override def add(a: Point, b: Point): Point = Point(a.x + b.x, a.y + b.y)
  }

  def sum2[A](xs: List[A])(implicit adder: Adder[A]): A =
    xs.foldLeft(adder.zero)(adder.add)

  implicit val intAdder2 = new Adder[Int] {
    override def zero: Int = 0
    override def add(x: Int, y: Int): Int = x + y
  }
  implicit val pointAdder2 = new Adder[Point] {
    override def zero: Point = Point(0, 0)
    override def add(a: Point, b: Point): Point = Point(a.x + b.x, a.y + b.y)
  }

  def main(args: Array[String]): Unit = {
//    println(sum(List(1, 2, 3)))
//    println(sum(List(Point(1, 10), Point(5, 5))))

    println(sum(List(1, 2, 3))(intAdder))
    println(sum(List(Point(1, 10), Point(5, 5)))(pointAdder))

    println(sum2(List(1, 2, 3)))
    println(sum2(List(Point(1, 10), Point(5, 5))))
  }
}
