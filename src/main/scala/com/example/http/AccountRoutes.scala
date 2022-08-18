package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.example.actors._

import java.net.http.HttpClient
import java.sql
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
import java.time.LocalDate
import com.example.dao.Connection
import scala.util.{Success, Failure, Try}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import com.example.dao.MovieService.MovieService._




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



  val accountRoutes: Route =
    pathPrefix("account") {
      path(IntNumber) { int =>
        post {
          println("Posting")
          complete(if (int % 2 == 0) "even ball" else "odd ball")
        } ~
          get {
            import com.example.generated.models.Tables._
            val movieRow = new MovieRow(movieId = 1L,name = "Star dsfsddfd", releaseDate = new sql.Date(1994,4,5), lengthInMin = 124)
            insertMovie(movieRow)
            updateMovieName("Star dsfsddfd", "dsjqdijdidjiqsjdiqsjdiqsjdiq")
            getAll("Hi")
            sideEffect()
            complete("Inside getter")

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
