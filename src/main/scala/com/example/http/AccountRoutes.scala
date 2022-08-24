package com.example

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.example.actors._

import java.sql
import scala.concurrent.Future
import com.example.actors.AccountActor._
import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import com.example.dao.MovieService.MovieService._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport



//#import-json-formats
//#user-routes-class

object PrivateExecutionContext {
  val executor = Executors.newFixedThreadPool(4);
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutorService((executor))
}

case class AccountEntity(id: Option[Long] = None, name: String, surname: String, subscription: String)


class AccountRoutes(accountRegistry: ActorRef[AccountActor.Command])(implicit
    val system: ActorSystem[_]
) extends FailFastCirceSupport {

  import PrivateExecutionContext._
  import akka.http.scaladsl.model.{ HttpRequest, RequestEntity }

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
  import io.circe.generic.auto._


  val accountRoutes: Route =
    pathPrefix("account") {
      post {
        entity(as[AccountEntity]) {
          accountEntity: AccountEntity => {
            println(accountEntity)
            complete("Hiya")
          }
        }
      } ~
        path(IntNumber) { int =>
          post {
            println("Posting")
            complete(if (int % 2 == 0) "even ball" else "odd ball")
          } ~
            get {
              import com.example.generated.models.Tables._
              val movieRow = new MovieRow(movieId = 1L, name = "Star dsfsddfd", releaseDate = new sql.Date(1994, 4, 5), lengthInMin = 124)
              insertMovie(movieRow)
              updateMovieName("Star dsfsddfd", "dsjqdijdidjiqsjdiqsjdiqsjdiq")
              getAll("Hi")
              sideEffect()
              complete("Inside getter")
            }
          }
      }
    }

