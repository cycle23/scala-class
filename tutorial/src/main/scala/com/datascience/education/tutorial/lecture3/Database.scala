package com.datascience.education.tutorial.lecture3


import cats.data.Reader
import cats.syntax.applicative._
import cats.{Id, MonadReader}

import scala.collection.mutable.{Set => MutableSet}
import scala.collection.mutable.{HashSet => MutableHashSet}
import scala.collection.mutable.{Map => MutableMap}
import scala.collection.mutable.HashMap
import org.mindrot.jbcrypt.BCrypt

import scala.util.Random
/*
 Note that in Reader, the `S` is static
 In `State`, `S` is intended to change.
 So this example does not "pass along" new copies of an immutable database
 */

sealed trait Database {

  // user ID, username
  val users: MutableMap[Int, String]

  // username, (salt, hash)
  val passwords: MutableMap[String, (String,String)]

  def genId: Int

  override def toString: String = {
    val userString = users.toList.map(_.toString + '\n').mkString
    val pwString = passwords.toList.map(_.toString + '\n').mkString

    "Users: "+userString+" passwords: "+pwString

  }
}

case class TestDatabase() extends Database {
  // user ID, username
  val users: MutableMap[Int, String] = MutableMap.empty[Int,String]

  // username, (salt, hash)
  val passwords: MutableMap[String, (String, String)] =
    MutableMap.empty[String,(String, String)]

  val rand = new Random()

  // unsafe if all ints used...
  @annotation.tailrec
  final def genId: Int = {
    val r = rand.nextInt().abs
    if (users.contains(r))
      genId
    else
      r
  }
}


object DatabaseQueriesAndUpdates {


  // Task 2a
  type DatabaseReader[A] = Reader[Database, A]
  // Task 2b
  def findUsername(userId: Int): DatabaseReader[String] = {
    Reader(Database => Database.users(userId))
  }


  // Task 2c
  def findUserId(username: String): DatabaseReader[Int] = {
    Reader(Database => Database.users.find((entry) => entry._2 == username).getOrElse((-1,""))._1)
  }

  // Task 2d
  def userExists(username: String): DatabaseReader[Boolean] = {
    findUserId(username).andThen(x => x != -1)
  }

  // Task 2e
  def checkPassword(username: String, passwordClear: String): DatabaseReader[Boolean] = {
    Reader(Database => {
      val (_, pwdHash) = Database.passwords(username)
      BCrypt.checkpw(passwordClear, pwdHash)
    })
  }

  // Task 2f
  def checkLogin(userId: Int, passwordClear: String): DatabaseReader[Boolean] = {
    findUsername(userId).flatMap((x) => checkPassword(x, passwordClear))
  }

  // Task 2g
  def createUser(username: String, passwordClear: String): DatabaseReader[Int] = {
    findUserId(username).flatMap(id => {
      id match {
        case -1 => Reader(Database => {
          val id = Database.genId
          Database.users(id) = username
          val salt = BCrypt.gensalt()
          Database.passwords(username) = (salt,BCrypt.hashpw(passwordClear, salt))
          id})
        case _ => Reader(Database => id)
      }
    })
  }

}


