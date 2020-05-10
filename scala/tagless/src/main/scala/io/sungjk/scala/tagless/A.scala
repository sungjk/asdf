package io.sungjk.scala.tagless

object A extends App {
	trait Language[F[_]] {
		def number(a: Int): F[Int]
		def increment(a: F[Int]): F[Int]
		def add(a: F[Int], b: F[Int]): F[Int]
		
		def text(a: String): F[String]
		def toUpper(a: F[String]): F[String]
		def concat(a: F[String], b: F[String]): F[String]
		
		def toString(a: F[Int]): F[String]
	}
	
	trait ScalaToLanguageBridge[S] {
		def apply[F[_]](implicit L: Language[F]): F[S]
	}
	
	def buildNumbber(number: Int) = new ScalaToLanguageBridge[Int] {
		override def apply[F[_]](implicit L: Language[F]): F[Int] = L.number(number)
	}
	
	def buildIncrementNumber(number: Int) = new ScalaToLanguageBridge[Int] {
		override def apply[F[_]](implicit L: Language[F]): F[Int] = L.increment(L.number(number))
	}
	
	def buildIncrementExpression(expression: ScalaToLanguageBridge[Int]) = new ScalaToLanguageBridge[Int] {
		override def apply[F[_]](implicit L: Language[F]): F[Int] = L.increment(expression.apply)
	}
	
	// println(s"$text ${a + (b + 1)}")
	def buildComplexExpression(text: String, a: Int, b: Int) = new ScalaToLanguageBridge[String] {
		override def apply[F[_]](implicit L: Language[F]): F[String] = {
			val addition = L.add(L.number(a), L.increment(L.number(b)))
			L.concat(L.text(text), L.toString(addition))
		}
	}
	
	val basicExpression = buildIncrementExpression(buildIncrementNumber(40))
	val complexExpression = buildComplexExpression("Result is ", 40, 1)
	
	type NoWrap[S] = S
	val interpret = new Language[NoWrap] {
		override def number(a: Int): NoWrap[Int] = a
		override def increment(a: NoWrap[Int]): NoWrap[Int] = a + 1
		override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a + b
		
		override def text(a: String): NoWrap[String] = a
		override def toUpper(a: NoWrap[String]): NoWrap[String] = a.toUpperCase
		override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = a + b
		
		override def toString(a: NoWrap[Int]): NoWrap[String] = a.toString
	}
	
	println(s"interpreted basic: ${basicExpression.apply(interpret)}")
	println(s"interpreted complex: ${complexExpression.apply(interpret)}")
	
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
	
	println(s"interpreted basic (as pretty print): ${basicExpression.apply(interpretAsPrettyPrint)}")
	println(s"interpreted complex (as pretty print): ${complexExpression.apply(interpretAsPrettyPrint)}")
	
	import scala.concurrent.ExecutionContext.Implicits.global
	import scala.concurrent.Future
	
	val interpretAsAsync = new Language[Future] {
		override def number(a: Int): Future[Int] = Future.successful(a)
		override def increment(a: Future[Int]): Future[Int] =
			for {
				av <- a
			} yield av + 1
		override def add(a: Future[Int], b: Future[Int]): Future[Int] =
			for {
				av <- a
				bv <- b
			} yield av + bv
		
		override def text(a: String): Future[String] = Future.successful(a)
		override def toUpper(a: Future[String]): Future[String] =
			for {
				av <- a
			} yield av.toUpperCase
		override def concat(a: Future[String], b: Future[String]): Future[String] =
			for {
				av <- a
				bv <- b
			} yield av + bv
		
		override def toString(a: Future[Int]): Future[String] = a.map(_.toString)
	}
	
	println(s"interpreted basic (as Future): ${basicExpression.apply(interpretAsAsync)}")
	println(s"interpreted complex (as Future): ${complexExpression.apply(interpretAsAsync).map(_.toString)}")
}
