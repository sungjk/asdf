package io.sungjk.scala.tagless

import io.sungjk.scala.tagless.A.{Language, ScalaToLanguageBridge}

object C extends App {
	def buildIncrementExpression() = new ScalaToLanguageBridge[Int] {
		override def apply[F[_]](implicit L: A.Language[F]): F[Int] =
			L.add(L.number(10), L.increment(L.increment(L.increment(L.number(0)))))
	}
	
	type NoWrap[S] = S
	val interpret = new Language[NoWrap] {
		override def number(a: Int): NoWrap[Int] = a
		override def increment(a: NoWrap[Int]): NoWrap[Int] = a + 1
		override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a + b
		
		override def text(a: String): NoWrap[String] = a
		override def toUpper(a: NoWrap[String]): NoWrap[String] = a.toLowerCase
		override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = a + b
		
		override def toString(a: NoWrap[Int]): NoWrap[String] = a.toString
	}
	
	type PrettyPrint[S] = String
	val interpretAsPrettyPrint = new Language[PrettyPrint] {
		override def number(a: Int): PrettyPrint[Int] = s"($a)"
		override def increment(a: PrettyPrint[Int]): PrettyPrint[Int] = s"(inc $a)"
		override def add(a: PrettyPrint[Int], b: PrettyPrint[Int]): PrettyPrint[Int] = s"(+ $a $b)"
		
		override def text(a: String): PrettyPrint[String] = s"($a)"
		override def toUpper(a: PrettyPrint[String]): PrettyPrint[String] = s"(toUpper $a)"
		override def concat(a: PrettyPrint[String], b: PrettyPrint[String]): PrettyPrint[String] = s"(concat $a $b)"
		
		override def toString(a: PrettyPrint[Int]): PrettyPrint[String] = s"(toString $a)"
	}
	
	val simpleVersion = buildIncrementExpression()
	// (+ (10) (inc (inc (inc (0)))))
	println(s"Unoptimized: ${simpleVersion.apply(interpret)} = ${simpleVersion.apply(interpretAsPrettyPrint)}")
	
	type Nested[ScalaValue] = ScalaToLanguageBridge[ScalaValue]
	val simplify = new Language[Nested] {
		var nesting = 0
		
		override def number(a: Int): Nested[Int] = new ScalaToLanguageBridge[Int] {
			override def apply[F[_]](implicit L: Language[F]): F[Int] = {
				if (nesting > 0) {
					val temp = nesting
					nesting = 0
					L.add(L.number(temp), L.number(a))
				} else {
					L.number(a)
				}
			}
		}
		override def increment(a: ScalaToLanguageBridge[Int]): Nested[Int] = new ScalaToLanguageBridge[Int] {
			override def apply[F[_]](implicit L: Language[F]): F[Int] = {
				nesting = nesting + 1
				a.apply(L)
			}
		}
		override def add(a: ScalaToLanguageBridge[Int], b: ScalaToLanguageBridge[Int]): Nested[Int] = new ScalaToLanguageBridge[Int] {
			override def apply[F[_]](implicit L: Language[F]): F[Int] = {
				if (nesting > 0) {
					val temp = nesting
					nesting = 0
					L.add(L.number(temp), L.add(a.apply(L), b.apply(L)))
				} else {
					L.add(a.apply(L), b.apply(L))
				}
			}
		}
		
		override def text(a: String): Nested[String] = ???
		override def toUpper(a: Nested[String]): Nested[String] = ???
		override def concat(a: Nested[String], b: Nested[String]): Nested[String] = ???
		
		override def toString(a: Nested[Int]): Nested[String] = ???
	}
	
	val example1 = simpleVersion.apply(simplify)
	// (+ (10) (+ (3) (0)))
	println(s"Example 1: ${example1.apply(interpret)} = ${example1.apply(interpretAsPrettyPrint)}")
	
	val example2 = new ScalaToLanguageBridge[Int] {
		override def apply[F[_]](implicit L: Language[F]): F[Int] =
			// ((0 + 1) + (0 + 1 + 1)) + 1
			L.increment(L.add(L.increment(L.number(0)), L.increment(L.increment(L.number(0)))))
	}
	// (inc (+ (inc (0)) (inc (inc (0)))))
	println(s"Example 2: ${example2.apply(interpret)} = ${example2.apply(interpretAsPrettyPrint)}")
	// (+ (1) (+ (+ (1) (0)) (+ (2) (0))))
	println(s"Example 2: ${example2.apply(simplify).apply(interpret)} = ${example2.apply(simplify).apply(interpretAsPrettyPrint)}")
}
