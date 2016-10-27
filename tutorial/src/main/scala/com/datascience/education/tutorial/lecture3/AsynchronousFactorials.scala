package com.datascience.education.tutorial.lecture3

import scala.concurrent._

import scala.concurrent.ExecutionContext.Implicits.global

object AsynchronousFactorial {

  def factorial(n: Int): Int = {
    val fact = if(n == 0) 1 else n*factorial(n-1)

    Thread.sleep(500)
    println(s"factorial($n) = $fact")

    fact
  }

  def factorialAsync(n: Int): Future[Int] = Future(factorial(n))

  def printFactorial(n: Int): Future[Int] = {
    val fut: Future[Int] = factorialAsync(n)

    fut.onSuccess {
      case f => println(s"factorial $n is $f")
    }

    fut
  }

}

object AsynchronousFactorialsExample extends App {

  import AsynchronousFactorial._

  val fut10 = printFactorial(10)
  val fut20 = printFactorial(20)

  (1 to 30).foreach(i => {Thread.sleep(500); println(s"unrelated: $i")})

}

import cats.syntax.applicative._
import cats.syntax.writer._
import cats.data.Writer
import cats.instances.all._

object FactorialWriter {

  // Task (1a)

  type Logged[A] = Writer[Vector[String],A]
  object Logged {
    def apply[A](l: Vector[String], a: A) = new Logged[A](l, a)
  }

  // Task (1b)
  def factorial(n: Int): Logged[Int] = {
     val fact = 
         if(n == 0) Logged[Int](Vector(s"BASELINE"),1)
         else factorial(n-1).flatMap(v => Logged[Int](Vector(s"factorial for ($n) = $n * $v"), n*v))
     Thread.sleep(500)
     fact
  }


  // Task 1c
  def factorialAsync(n: Int): Future[Logged[Int]] = Future(factorial(n))


}

object FactorialWriterExample extends App {
  import FactorialWriter._

  factorial(1)
  factorial(2)
  factorial(20)
  (1 to 30).foreach(i => {Thread.sleep(500); println(s"unrelated: $i")})
}

object FactorialWriterAsyncExample extends App {
  import FactorialWriter._


  // Task 1d

  val y = factorialAsync(2)
  val z = factorialAsync(10)
  val zz = factorialAsync(20)
  val x = factorialAsync(1)
  (1 to 30).foreach(i => {Thread.sleep(500); println(s"unrelated: $i")})
  x onSuccess {
    case f => println(f.run._1 + "..." + f.run._2)
  }
  z onSuccess {
    case f => println(f.run._1 + "..." + f.run._2)
  }
  zz onSuccess {
    case f => println(f.run._1 + "..." + f.run._2)
  }
}

