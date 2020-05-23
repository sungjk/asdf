package io.sungjk.scala.lang

// https://github.com/ikhoon/share-scala/blob/master/type-level-programming.md
sealed trait Bool
sealed trait True extends Bool
sealed trait False extends Bool

sealed trait BuildState {
	type Id <: Bool
	type Name <: Bool
	type DisplayName <: Bool
}

case class Item(id: Int, name: String, displayName: String, imageUrl: Option[String])

class Builder[B <: BuildState] { self =>
	private var id: Option[Int] = None
	private var name: Option[String] = None
	private var displayName: Option[String] = None
	private var imageUrl: Option[String] = None

	def newBuilder[C <: BuildState] = this.asInstanceOf[Builder[C]]

	def withId(id: Int) = {
		self.id = Some(id)
		newBuilder[B {type Id = True}]
	}

	def withName(name: String) = {
		self.name = Some(name)
		newBuilder[B {type Name = True}]
	}

	def withDisplayName(displayName: String) = {
		self.displayName = Some(displayName)
		newBuilder[B {type DisplayName = True}]
	}

	def withImageUrl(imageUrl: String) = {
		self.imageUrl = Some(imageUrl)
		newBuilder[B]
	}

	def build(implicit
		ev1: B#Id =:= True,
		ev2: B#Name =:= True,
		ev3: B#DisplayName =:= True
	): Item =
		Item(id.get, name.get, displayName.get, imageUrl)
}

object Builder {
	def apply() = new Builder[BuildState {}]()
}

object PhantomTypeBuilder {
	def main(args: Array[String]): Unit = {
		// 필수값을 설정하지 않아서 컴파일 되지 않는다
//		Builder().build
//		Builder().withId(42).build

		val item = Builder()
  		.withId(42)
  		.withName("foo")
  		.withDisplayName("bar")
  		.build
		println(item)

		val item2 = Item(
			id = 42,
			name = "foo",
			displayName = "bar",
			imageUrl = None
		)
		println(item2)

		assert(item == item2)
	}
}
