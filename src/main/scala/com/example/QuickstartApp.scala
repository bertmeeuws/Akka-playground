package com.example

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.example.actors._
import com.example.http.ChatAPI

import scala.util.Failure
import scala.util.Success

//#main-class
object QuickstartApp {
  // #start-http-server
  private def Start(
      routes: Route
  )(implicit system: ActorSystem[_]): Unit = {
    // Akka HTTP still needs a classic ActorSystem to start
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 3001).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info(
          "Server online at http://{}:{}/",
          address.getHostString,
          address.getPort
        )
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }
  // #start-http-server
  def main(args: Array[String]): Unit = {
    // #server-bootstrapping
    /*
    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val userRegistryActor = context.spawn(UserRegistry(), "UserRegistryActor")
      context.watch(userRegistryActor)

      // In akka you cant create an actor using the New keyword
      // doesn't reture an actor but will return a reference to the actor
      val accountRegistryActor =
        context.spawn(AccountActor(), "AccountRegistryActor")
      context.watch(accountRegistryActor)

      val userRoutes = new UserRoutes(userRegistryActor)(context.system)
      val accountRoutes =
        new AccountRoutes(accountRegistryActor)(context.system)

      val topLayerRoutes: Route =
        concat(userRoutes.userRoutes, accountRoutes.accountRoutes)

      startHttpServer(topLayerRoutes)(context.system)

      Behaviors.empty
    }
    */

    val rootBehavior = Behaviors.setup[Nothing]{ context =>
      val store = context.spawn(ChatsStore(), "Store")
      val api = new ChatAPI(store)(context.system)
      Start(api.routes)(context.system)
      Behaviors.same
    }
    ActorSystem[Nothing](rootBehavior, "ChatApp")

  }
}
//#main-class
