import scala.language.higherKinds
import cats._
import cats.data._
//import cats.data.Writer
//import cats.instances.vector._
//import cats.syntax.applicative._
import cats.implicits._
type Logged[A] = Writer[Vector[String],A]
val a = Writer(Vector("msg1","msg2"),123)
val b = 123.writer(Vector("msg1", "msg2"))
val result = a.value
val log = a.written
a.run
val writer1 = for {
  a <- 10.pure[Logged]
  _ <- Vector("a", "b").tell
  b <- 32.writer(Vector("x","y"))
} yield a + b
writer1.mapWritten(_.map(_.toUpperCase)).run
val writer3 = writer1.swap
for {
  a <- writer3
  _ <- 10.tell
} yield a
def double(a: Int): Int = a*2
val doubleReader: Reader[Int,Int] = Reader(double)
doubleReader.run(21)
def addKReader(k: Int): Reader[Int, Int] = Reader(_ + k)
val foo = doubleReader.flatMap(addKReader)
foo.run(14)
val addReaders: Reader[Int, Int] =
  for {
    x <- doubleReader
    y <- addKReader(x)
  } yield x + y
addReaders.run(10)
val sub5Reader: Reader[Int, Int] = Reader(_ - 5)
val sequencingEx: Reader[Int, (Int, Int)] =
  for {
    x <- doubleReader
    y <- if(x < 20) sub5Reader else addKReader(x)
  } yield (x, y)
sequencingEx.run(5)
sequencingEx.run(15)
