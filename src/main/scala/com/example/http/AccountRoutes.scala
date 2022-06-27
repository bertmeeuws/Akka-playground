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

//#import-json-formats
//#user-routes-class
class AccountRoutes(accountRegistry: ActorRef[AccountActor.Command])(implicit
    val system: ActorSystem[_]
) {

  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import JsonFormats._

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
      concat(
        path("id") {
          post {
            entity(as[Multipart.FormData]) { formData =>
              implicit val ec: scala.concurrent.ExecutionContext =
                scala.concurrent.ExecutionContext.global

              println(formData)

              println(formData.getMediaType())

              // collect all parts of the multipart as it arrives into a map
              val allPartsF: Future[Map[String, Any]] = formData.parts
                .mapAsync[(String, Any)](1) {

                  case b: Multipart.BodyPart if b.name == "file" =>
                    println("inside bodypart1")
                    // stream into a file as the chunks of it arrives and return a future
                    // file to where it got stored
                    val file = File.createTempFile("upload", "tmp")
                    b.entity.dataBytes
                      .runWith(FileIO.toPath(file.toPath))
                      .map(_ => (b.name -> file))

                  case b: Multipart.FormData.BodyPart =>
                    println("inside bodypart2")
                    // collect form field values
                    b.toStrict(new FiniteDuration(2, SECONDS))
                      .map(strict => (b.name -> strict.entity.data.utf8String))

                }
                .runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

              val done1 = Await.result(allPartsF, Duration(5, "seconds"))

              println(done1)

              val done = allPartsF.map { allParts =>
                // You would have some better validation/unmarshalling here
                /*
        db.create(Video(
          file = allParts("file").asInstanceOf[File],
          title = allParts("title").asInstanceOf[String],
          author = allParts("author").asInstanceOf[String]))
                 */
              }

              complete("Done")

              // when processing have finished create a response for the user
              onSuccess(allPartsF) { allParts =>
                complete {
                  "ok!"
                }
              }
            }
          }
        },
        concat(
          pathEnd(
            concat(
              get {
                println("getting accounts")
                complete("Getting accounts")
              },
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
            )
          )
        )
      )
    }
}
