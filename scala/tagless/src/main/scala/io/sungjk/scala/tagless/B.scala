package io.sungjk.scala.tagless

import io.sungjk.scala.tagless.A.{Language, NoWrap}

object B extends App {
	trait LanguageWithMul[F[_]] extends Language[F] {
		def multiply(a: F[Int], b: F[Int]): F[Int]
	}
	
	trait ScalaTolanguageWithMulBridge[S] {
		def apply[F[_]](implicit L: LanguageWithMul[F]): F[S]
	}
	
	def multiply(a: Int, b: Int) = new ScalaTolanguageWithMulBridge[Int] {
		override def apply[F[_]](implicit L: LanguageWithMul[F]): F[Int] =
			L.multiply(L.number(a), L.number(b))
	}
	
	val multiplyExpression = multiply(2, 3)
	val interpretWithMul = new LanguageWithMul[NoWrap] {
		override def multiply(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b
		
		override def number(a: Int): NoWrap[Int] = a
		override def increment(a: NoWrap[Int]): NoWrap[Int] = a + 1
		override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a + b
		
		override def text(a: String): NoWrap[String] = a
		override def toUpper(a: NoWrap[String]): NoWrap[String] = a.toUpperCase
		override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = a + b
		
		override def toString(a: NoWrap[Int]): NoWrap[String] = a.toString
	}
	
	println(s"interpreted multiply: ${multiplyExpression.apply(interpretWithMul)}")
}
