package com.example

import com.example.UserRegistry.ActionPerformed
import com.example.actors._

//#json-formats
import spray.json.DefaultJsonProtocol
import spray.json._
import spray.json.DeserializationException
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import slick.model.Table

object JsonFormats {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  // implicit val accountJsonFormat = jsonFormat4(Account)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}

object Parser {
  def parseCurrency(value: String): Currency = value match {
    case "EUR" => EUR()
    case "USD" => USD()
    case _     => throw new DeserializationException("Currency is not avaiable")
  }
}

object AccountJsonFormat extends DefaultJsonProtocol with SprayJsonSupport {
  implicit object AccountJsonFormat extends RootJsonFormat[Account] {
    def write(obj: Account): JsValue = JsObject(
      "id" -> JsString(obj.id),
      "name" -> JsString(obj.name),
      "balance" -> JsString(obj.balance.toString()),
      "currency" -> JsString(obj.currency.toString())
    )
    def read(value: JsValue): Account = {
      val fields =
        value.asJsObject.getFields("id", "name", "balance", "currency");

      val acc: Account =
        value.asJsObject.getFields("id", "name", "balance", "currency") match {
          case Seq(
                JsString(id),
                JsString(name),
                JsString(balance),
                JsString(currency)
              ) => {
            if (balance.contains(","))
              throw new DeserializationException("Use dots instead of commas")
            val account = Account(
              id,
              name,
              balance.toDouble,
              Parser.parseCurrency(currency)
            )
            account
          }
          case _ => throw new DeserializationException("Expected an account")
        }
      acc
    }
  }
}

import com.example.dao.Connection
import com.example.models.codegen.Tables
import com.example.models.codegen.Tables._
import slick.jdbc.PostgresProfile.api._


object MovieJsonFormat extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object MovieJsonFormat extends RootJsonFormat[Tables] {
    def write(obj: Account): JsValue = JsObject(
      "id" -> JsString(obj.id),
      "name" -> JsString(obj.name),
      "balance" -> JsString(obj.balance.toString()),
      "currency" -> JsString(obj.currency.toString())
    )
  }
}

//#json-formats
