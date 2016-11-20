package com.datascience.education.tutorial.lecture4a

import scala.util.Random

import cats.data.Xor
import cats.data.Xor.Left
import cats.data.Xor.Right

object ResponseList {

  import RequestResponse._
  import TraverseXor._

  val rand = new Random

  val listRequests = (1 to 10).map((i: Int) => Request(i.toString)).toList
  val listCorrupt = Request("foo") :: listRequests ::: List(Request("bar"), Request("5"))

  // Task 4a
  def parsePayload(payload: Payload): Xor[NumberFormatException, Int] =
    Xor.catchOnly[NumberFormatException](payload.pay.toInt)

  // Task 4b
  def pipeline(request: Request): Xor[Exception, Int] =
    client(request) flatMap parsePayload

  // Task 4c
  def sum(lr: List[Request]): XorException[Int] =
    TraverseXor.traverse(lr)(pipeline).map(x => x.sum)
}

object ResponseListExample extends App {
  import ResponseList._

  println("List of valid Requests:")
  println(listRequests)


  val sumValid = sum(listRequests)

  println(sumValid)

  println("----------------------")
  println("List of possibly corrupt Payloads:")
  println(listCorrupt)

  val sumCorrupt = sum(listCorrupt)

  println(sumCorrupt)

}
