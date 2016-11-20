package com.datascience.education.tutorial.lecture4a


object SafeDivisionOption {

  def safeDivInt(numerator: Int, denominator: Int): Option[Int] =
    try {
      Some(numerator / denominator)
    } catch {
      case ae: java.lang.ArithmeticException => None
    }

  def squareRootFrac(numerator: Int, denominator: Int): Option[Double] =
    safeDivInt(numerator, denominator).flatMap { _ =>
      val squareRoot: Double =
        math.sqrt(numerator.toDouble / denominator)
      if (squareRoot.isNaN)
        None
      else
        Some(squareRoot)
    }


  def squareRootFrac(tup: (Int, Int)): Option[Double] =
    squareRootFrac(tup._1, tup._2)


  import TraverseOption._
  
  def traverseFractions(ll: List[(Int, Int)]): Option[List[Double]] = 
    traverse(ll)(squareRootFrac)


}


object SafeDivisionOptionExamples extends App {

  import SafeDivisionOption._


  println("sqrt(7/2)")
  println(squareRootFrac(7,2))

  println("-----------------")

  println("sqrt(7/0)")
  println(squareRootFrac(7,0))

  println("-----------------")

  println("sqrt(-4/3)")
  println(squareRootFrac(-4,3))


}


import cats.data.Xor
import cats.data.Xor.Left
import cats.data.Xor.Right

object SafeDivisionXor {

  // Task 2a
  def safeDivInt(numerator: Int, denominator: Int): Xor[Exception, Int] =
    Xor.catchOnly[ArithmeticException](numerator/denominator)

  // Task 2b
  def squareRootFrac1(numerator: Int, denominator: Int): Xor[Exception, Double] =
  safeDivInt(numerator, denominator).map{x=>Math.sqrt(x)}.flatMap { x =>
    if (x.equals(Double.NaN)) Xor.left(new ArithmeticException("Naaaan"))
    else Xor.right(x)
  }

  def squareRootFrac(numerator: Int, denominator: Int):
      Xor[Exception, Double] = {
    val y = Math.sqrt(numerator.toDouble / denominator)
    if (y.equals(Double.NaN))
      Xor.left(new ArithmeticException("NaN is yummy flatbread"))
    else if (y == Double.PositiveInfinity || y == Double.NegativeInfinity)
      Xor.left(new ArithmeticException("Can't divide by zero, silly"))
    else
      Xor.right(y)
  }

  def squareRootFrac(tup: (Int, Int)): Xor[Exception, Double] =
    squareRootFrac(tup._1, tup._2)
}

object SafeDivIntXorExamples extends App {
  import SafeDivisionXor._

  println("7/2")
  println(safeDivInt(7,2))

  println("-----------------")

  println("7/0")
  println(safeDivInt(7,0))
}


object SquareRootFracXorExamples extends App {
  import SafeDivisionXor._

  println("sqrt(7/2)")
  println(squareRootFrac(7,2))

  println("-----------------")

  println("sqrt(7/0)")
  println(squareRootFrac(7,0))

  println("-----------------")

  println("sqrt(-4/3)")
  println(squareRootFrac(-4,3))
}




