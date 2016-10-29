package com.datascience.education.tutorial.lecture3

import org.scalatest._
import prop._

import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop.forAllNoShrink

import org.scalatest.prop.Checkers.check

import cats.data.Reader
import cats.syntax.applicative._

class DatabaseSpec extends PropSpec with
    GeneratorDrivenPropertyChecks with Matchers {


  import DatabaseQueriesAndUpdates._

  // Task 3a
  property("User does not exist in database") {

    val testDB = TestDatabase()

    forAll(Gen.alphaStr) {
      case (username: String) =>
        userExists(username).run(testDB) should be (false)
    }

  }


  // val nonEmptyString = Gen.nonEmptyContainerOf(Gen.alphaStr)

  val nonEmptyString = Gen.chooseNum(8, 32).flatMap { length =>
    Gen.listOfN(length, Gen.alphaChar).map(_.mkString)
  }


  // Task 3b
  property("create user") {
    val testDB = TestDatabase()

    forAll(nonEmptyString, nonEmptyString) {
      case (username: String, password: String) =>
        createUser(username, password).run(testDB) should not be (-1)
    }
  }


  // Task 3c
  property("User exists in database") {
    val testDB = TestDatabase()

    forAll(nonEmptyString, nonEmptyString) {
      case (username: String, password: String) =>
        createUser(username, password).run(testDB) should not be (-1)
        userExists(username).run(testDB) should be (true)
    }
  }

  // Task 3d
  property("Password works") {
    val testDB = TestDatabase()

    forAll(nonEmptyString, nonEmptyString) {
      case (username: String, password: String) =>
        createUser(username, password).run(testDB) should not be (-1)
        checkPassword(username, password).run(testDB) should be (true)
    }
  }


  // Task 3e
  property("Bad password fails") {
    val testDB = TestDatabase()

    forAll(nonEmptyString, nonEmptyString, nonEmptyString) {
      case (username: String, password: String, badPassword) =>
        createUser(username, password).run(testDB) should not be (-1)
        checkPassword(username, badPassword).run(testDB) should be (false )
    }
  }
}

