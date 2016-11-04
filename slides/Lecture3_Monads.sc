import scala.language.higherKinds
/*
import cats.{Id, Monad}
import cats.implicits._
import cats.syntax.flatMap._
*/
import cats._
import cats.data._
import cats.implicits._

val r0 = Monad[Option].pure(3)
val r1 = Monad[Option].flatMap(r0)(a => Some(a+2))
val r2 = Monad[List].pure(3)
val r3 = Monad[List].flatMap(r2)(x => List(x,x*10))
val r4 = 4.some >>= { x:Int => (x*2).some }
case class CountMe[A](count: Int, data: A)
val countMeMonad = new Monad[CountMe] {
  def flatMap[A, B](value: CountMe[A])
    (f: A => CountMe[B]): CountMe[B] = {
      val x: CountMe[B] = f(value.data)
      CountMe(value.count+x.count, x.data)}
  def pure[A](value: A): CountMe[A] = CountMe(0, value)
  def tailRecM[A, B](a: A)(f: A => CountMe[Either[A, B]]): CountMe[B]   = defaultTailRecM(a)(f)
}
val m1 = countMeMonad.pure(3)
val m2 = countMeMonad.pure(1)
countMeMonad.flatMap(m2)((x) => countMeMonad.pure(1))

//case class MyId[A](id: A)
val myIdMonad = new Monad[MyId] {
  def flatMap[A,B](value: MyId[A])
    (f: A => MyId[B]): MyId[B] = {
      f(value)
  }
  def pure[A](value: A): MyId[A] = value
  def tailRecM[A, B](a: A)(f: A => MyId[Either[A, B]]): MyId[B]   = defaultTailRecM(a)(f)
}
type MyId[A] = A
val a: Id[Int] = 3
val b: Id[Int] = a.flatMap(_ + 2)
val c: Id[Int] = a + 2
myIdMonad.flatMap(a)(_ + 2)
myIdMonad.flatMap("hi")(x => myIdMonad.map(" there!")(y => x + y))
//val c: MyId[Int] = myIdMonad.pure(a + 2)
