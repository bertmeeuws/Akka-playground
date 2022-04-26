package com.example


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.example.actors._

import scala.concurrent.Future
//import com.example.UserRegistry._
import com.example.actors.AccountActor._
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import scala.concurrent.duration.Duration
import scala.concurrent.Await

//#import-json-formats
//#user-routes-class
class AccountRoutes(accountRegistry: ActorRef[AccountActor.Command])(implicit val system: ActorSystem[_]) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._
  import AccountJsonFormat.AccountJsonFormat._


  //import JsonFormats._

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))


  //#import-json-formats
  def createAccount(account: Account): Future[Account] = accountRegistry.ask(CreateAccount())
  

  val accountRoutes: Route = 
    pathPrefix("account") {
      concat(
        pathEnd(
          concat(
            get{
              println("getting accounts")
              complete("Getting accounts")
            },
            post {
              entity(as[Account]) {
                account => onComplete(createAccount(account))
              }
            }
          )
          
        )
      )
    }


}
