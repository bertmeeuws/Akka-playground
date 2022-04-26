package com.example.actors

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import scala.collection.immutable



sealed trait Currency{
    def toString():String
}
final case class EUR() extends Currency{
    override def toString() = "EUR"
}
final case class USD() extends Currency{
    override def toString() = "EUR"
}



final case class Account(id: String, name: String, balance: Double, currency: Currency)
final case class Accounts(accounts: Seq[Account])


object AccountActor {

    sealed trait Command
    
    final case class GetAccounts(replyTo: ActorRef[Accounts]) extends Command
    final case class CreateAccount(account: Account, replyTo : ActorRef[ActionPerformed]) extends Command
    final case class GetAccountById(id: String, replyTo: ActorRef[GetAccountResponse]) extends Command


    final case class GetAccountResponse(maybeAccount: Option[Account])
    final case class ActionPerformed(description: String)


    def apply(): Behavior[Command] = registry(Set(Account("ahdqsiuds", "Bert", 25.40, EUR())))


    private def registry(accounts: Set[Account]): Behavior[Command] =
        Behaviors.receiveMessage {
            case GetAccounts(replyTo) => 
                replyTo ! Accounts(accounts.toSeq)
                Behaviors.same
            case CreateAccount(account, replyTo) => 
                replyTo ! ActionPerformed(s"User created")
                registry(accounts + account)
            case GetAccountById(id, replyTo) => 
                replyTo ! GetAccountResponse(accounts.find(_.id == id))
                Behaviors.same
            
        }


}