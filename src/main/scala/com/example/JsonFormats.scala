package com.example

import com.example.UserRegistry.ActionPerformed
import com.example.actors._

//#json-formats
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import spray.json.JsValue
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsNumber
import spray.json.DeserializationException

object JsonFormats  {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  //implicit val accountJsonFormat = jsonFormat4(Account)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}

object Parser{
  def parseCurrency(value: String) = value match {
    case "EUR" => EUR()
    case "USD" => USD()
    case _ => throw new DeserializationException("Currency is not avaiable")
  }
}

object AccountJsonFormat extends DefaultJsonProtocol{
  implicit object AccountJsonFormat extends RootJsonFormat[Account]{
    def write(obj: Account): JsValue = JsObject(
      "id" ->  JsString(obj.id),
      "name" ->  JsString(obj.name),
      "balance" -> JsString(obj.balance.toString()),
      "currency" -> JsString(obj.currency.toString())
    )
    def read(value: JsValue) = {
      value.asJsObject.getFields("id","name","balance","currency") match {
        case Seq(JsString(id), JsString(name), JsNumber(balance), JsString(currency))  => Account(id, name, balance.toDouble, Parser.parseCurrency(currency))
        case _ => throw new DeserializationException("Expected an account")
      }
    }
  }
}



//#json-formats
