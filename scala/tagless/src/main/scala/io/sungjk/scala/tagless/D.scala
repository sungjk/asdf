package io.sungjk.scala.tagless

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object D extends App {
	case class User(id: Int, name: String, age: Int)
	
	trait Language[F[_]] {
		type QueryObj
		case class Raw(q: QueryObj)
		case class WithFilter(q: QueryObj)
		case class WithPagination(q: QueryObj)
		
		def user(): F[Raw]
		def filterByIds(query: F[Raw], ids: Seq[Int]): F[WithFilter]
		def paginate(query: F[WithFilter], skip: Int, limit: Int): F[WithPagination]
		
		def run(query: F[WithPagination]): F[Seq[User]]
	}
	
	trait ScalaToLanguageBridge[ScalaValue] {
		def apply[F[_]](implicit L: Language[F]): F[ScalaValue]
	}
	
	val slickInterpreter = new Language[Future] {
		import slick.jdbc.H2Profile.api._
		
		class SlickUserTable(tag: Tag) extends Table[User](tag, "user") {
			def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
			def name: Rep[String] = column[String]("name")
			def age: Rep[Int] = column[Int]("age")
			override def * = (id, name, age) <> (User.tupled, User.unapply)
		}
		
		private val slickUserQuery = TableQuery[SlickUserTable]
		private val db = Database.forURL(
			url = "jdbc:h2:mem:test",
			driver = "org.h2.Driver",
			keepAliveConnection = true
		)
		private val setup = DBIO.seq(
			slickUserQuery.schema.create,
			slickUserQuery ++= Seq(
				User(1, "foo", 20),
				User(2, "bar", 30),
				User(3, "baz", 40)
			)
		)
		private val created = Await.result(db.run(setup), 5.seconds)
		println(s"Created: $created")
		
		override type QueryObj = Query[SlickUserTable, User, Seq]
		override def user(): Future[Raw] =
			Future.successful(Raw(slickUserQuery))
		override def filterByIds(query: Future[Raw], ids: Seq[Int]): Future[WithFilter] =
			query.map(_.q.filter(_.id inSet ids)).map(WithFilter)
		override def paginate(query: Future[WithFilter], skip: Int, limit: Int): Future[WithPagination] =
			query.map(_.q.drop(skip).take(limit)).map(WithPagination)
		override def run(query: Future[WithPagination]): Future[Seq[User]] =
			query.flatMap(finalQuery => db.run(finalQuery.q.result))
	}
	
	val findUser = new ScalaToLanguageBridge[Seq[User]] {
		override def apply[F[_]](implicit L: Language[F]): F[Seq[User]] = {
			val base = L.user()
			val full = L.paginate(L.filterByIds(base, Seq(1, 2, 3)), skip = 1, limit = 1)
			L.run(full)
		}
	}
	val result = Await.result(findUser.apply(slickInterpreter), 1.seconds)
	println(s"Found: $result")
}
