package com.example.http

import akka.actor.typed.scaladsl.AskPattern.{Askable, schedulerFromActorSystem}
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.example.actors.ChatsStore.{AddNewChat, GetChatMeta, GetChatMetaResponse, StoreCommand}
import com.example.http.ChatAPI.{ChatCreated, StartChat, chatCreatedEncoder, getChatMetaEncoder}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import java.util.UUID
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

object ChatAPI {
  case class StartChat(sender: User, receiver: User)

  case class User(id: UUID, name: String)

  case class ChatCreated(chatId: Int, senderChatLink: String, receiverChatLink: String)

  implicit val startChatDecoder: Decoder[StartChat] = deriveDecoder
  implicit val startChatEncoder: Encoder[StartChat] = deriveEncoder

  implicit val userDecoder: Decoder[User] = deriveDecoder
  implicit val userEncoder: Encoder[User] = deriveEncoder

  implicit val chatCreatedDecoder: Decoder[ChatCreated] = deriveDecoder
  implicit val chatCreatedEncoder: Encoder[ChatCreated] = deriveEncoder

  //@TODO Working on this
  implicit val getChatMetaDecoder: Decoder[GetChatMetaResponse] = deriveDecoder[GetChatMetaResponse]
  implicit val getChatMetaEncoder: Encoder[GetChatMetaResponse] = deriveEncoder[GetChatMetaResponse]
}

class ChatAPI(store: ActorRef[StoreCommand])(implicit val system: ActorSystem[_]) extends FailFastCirceSupport {

  private implicit val timeout: Timeout = Timeout(2.seconds)
  private implicit val ec: ExecutionContextExecutor = system.executionContext

  def RoomRoute(roomId: Int): Route = {
    pathEnd {
      post {
        entity(as[StartChat]) {start =>
          val senderId = start.sender.id.toString

          val asyncCreated = store.ask(ref => GetChatMeta(roomId, senderId, ref))

          onSuccess(asyncCreated) {meta =>
            println(s"RoomId: $roomId")
            println(meta)
            complete(StatusCodes.OK)
          }
        }
      }
    }
  }

  val routes: Route = {
    pathPrefix("chats") {
      concat(pathEnd {
        post {
          entity(as[StartChat]) { start =>

            val senderId = start.sender.id.toString
            val receiverId = start.receiver.id.toString

            val asyncCreated = store.ask(ref => AddNewChat(start.sender, start.receiver, ref)).map(id => {
              ChatCreated(id, "link1", "link2")
            })

            onSuccess(asyncCreated) { result =>
              println(senderId, receiverId)
              println("here")
              complete(StatusCodes.OK, result)
            }
          }
        }
      } ~ path("room" / IntNumber)(RoomRoute)
      )
    }
  }

  /*
  def websocketFlow(userName: String, chatActor: ActorRef[ChatCommand]): Flow[Message, Message, Any] = {
    val source: Source[TextMessage, Unit] =
      ActorSource.actorRef[String](PartialFunction.empty, PartialFunction.empty, 5, OverflowStrategy.fail)
        .map[TextMessage](TextMessage(_))
        .mapMaterializedValue(sourceRef => chatActor ! AddNewUser(sourceRef))

    val sink: Sink[Message, Future[Done]] = Sink
      .foreach[Message] {
        case tm: TextMessage =>
          chatActor ! ProcessMessage(userName, tm.getStrictText)
        case _ =>
          logger.warn(s"User with id: '{}', send unsupported message", userName)
      }

    Flow.fromSinkAndSource(sink, source)
  }
  */

}

