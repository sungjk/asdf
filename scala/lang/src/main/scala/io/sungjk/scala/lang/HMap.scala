package io.sungjk.scala.lang

trait Key { type Value }

trait HMap { self =>
  val underlying: Map[Any, Any]

  def get(key: Key): Option[key.Value] =
    underlying.get(key).map(_.asInstanceOf[key.Value])

  def add(key: Key)(value: key.Value): HMap =
    new HMap {
      override val underlying: Map[Any, Any] = self.underlying + (key -> value)
    }
}

object HMap {
  val empty = new HMap {
    override val underlying: Map[Any, Any] = Map.empty
  }
}

object RunHMap {
  def main(args: Array[String]): Unit = {
    // String이 Map에 Value 타입으로 들어오면 Any.
//    val map1: Map[String, Int] = Map("width" -> 42)
//    val map2: Map[String, Any] = map1 + ("sort" -> "time")
//    val width: Option[Any] = map2.get("width")
//    val sort: Option[Any] = map2.get("sort")

    val width = new Key { type Value = Int }
    val sort = new Key { type Value = String }
    val hmap: HMap = HMap.empty
      .add(width)(120)
      .add(sort)("time")
    val intOpt: Option[Int] = hmap.get(width)
    val stringOpt: Option[String] = hmap.get(sort)

    println(intOpt)
    println(stringOpt)
  }
}