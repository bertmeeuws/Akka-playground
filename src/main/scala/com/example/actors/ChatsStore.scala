package com.example.actors

import akka.actor.typed.{ActorRef, Behavior}
import com.example.actors.Chat.ChatCommand
import com.example.http.ChatAPI.User
import akka.actor.typed.scaladsl.Behaviors
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.collection.mutable

object ChatsStore extends FailFastCirceSupport {
  sealed trait StoreCommand
  final case class AddNewChat(sender: User, receiver: User, replyTo: ActorRef[Int]) extends StoreCommand
  final case class GetChatMeta(chatId: Int, userName: String, replyTo: ActorRef[Option[GetChatMetaResponse]]) extends StoreCommand


  final case class GetChatMetaResponse(userName: String, ref: ActorRef[ChatCommand])

  case class RoomResponse(roomName: String)

  private var sequence = 0
  private val store = mutable.Map.empty[Int, ChatMetadata]


  private case class ChatMetadata(participants: Map[String, User], ref: ActorRef[ChatCommand]) {
    def containUserId(userId: String): Boolean =
      participants.contains(userId)
  }

  def apply(): Behavior[StoreCommand] =
    Behaviors.setup(context => {
      Behaviors.receiveMessage {
        case AddNewChat(sender, receiver, replyTo) =>
          sequence += 1
          val newChat: ActorRef[ChatCommand] = context.spawn(Chat(), s"Chat$sequence")
          val participants = Map(sender.id.toString -> sender, receiver.id.toString -> receiver)
          val metadata = ChatMetadata(participants, newChat)
          println("Inside add new chat")
          store.put(sequence, metadata)
          replyTo ! sequence
          Behaviors.same
        case GetChatMeta(chatId, userId, replyTo) =>
          val chatRef = store
            .get(chatId)
            .filter(_.containUserId(userId))
            .flatMap(meta =>
              meta.participants
                .get(userId)
                .map(user => GetChatMetaResponse(user.name, meta.ref))
            )
          replyTo ! chatRef
          Behaviors.same
      }
    })

}