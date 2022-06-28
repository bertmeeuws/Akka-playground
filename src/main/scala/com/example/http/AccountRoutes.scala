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
import java.io._
import akka.stream._
import akka.http.scaladsl.model._
import akka.stream.scaladsl.FileIO
import java.util.Date
import scala.concurrent.duration._
import com.example.models.Movie
import java.time.LocalDate
import com.example.models.SlickTables
import com.example.dao.Connection
import scala.util.{Success, Failure, Try}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

//#import-json-formats
//#user-routes-class

object PrivateExecutionContext {
  val executor = Executors.newFixedThreadPool(4);
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService((executor))
}

class AccountRoutes(accountRegistry: ActorRef[AccountActor.Command])(implicit
    val system: ActorSystem[_]
) {
  import PrivateExecutionContext._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

  import slick.jdbc.PostgresProfile.api._

  import AccountJsonFormat.AccountJsonFormat

  // import JsonFormats._
  private implicit val timeout = Timeout.create(
    system.settings.config.getDuration("my-app.routes.ask-timeout")
  )

  // #import-json-formats
  def createAccount(account: Account): Future[ActionPerformed] =
    accountRegistry.ask(CreateAccount(account, _))

  def insertMovie() = {
    // import slick.jdbc.PostgresProfile.api._
    val batman = Movie(2L, "Batman", LocalDate.of(2022, 4, 1), 120)
    val queryDescription = SlickTables.movieTable += batman

    val futureId: Future[Int] = Connection.db.run(queryDescription)

    futureId.onComplete {
      case Success(value) =>
        complete("The query was succesfull");
      case Failure(ex) => complete("Error occured")
    }
  }
  val accountRoutes: Route =
    pathPrefix("account") {
      path(IntNumber) { int =>
        post {
          println("Posting")
          insertMovie()
          complete(if (int % 2 == 0) "even ball" else "odd ball")
        } ~
          get {
            println("inside get")
            complete("Inisde getterr")
          }
      } ~
        post {
          println("insidepost")
          entity(as[Account]) { account =>
            {
              println("succes??")
              println(account)
              onSuccess(createAccount(account)) { performed =>
                {
                  println("created")
                  complete("Great success")
                }

              }
            }
          }
        }

    }
}
