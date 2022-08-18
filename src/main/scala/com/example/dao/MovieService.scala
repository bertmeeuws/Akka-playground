package com.example.dao.MovieService

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import java.util.concurrent.Executors
import com.example.generated.models.Tables._
import slick.jdbc.PostgresProfile.api._
import com.example.dao.Connection.db

import scala.concurrent.{Await, ExecutionContext, Future}
import slick.lifted

import java.lang.ProcessBuilder.Redirect
import java.net.{Authenticator, InetSocketAddress, ProxySelector}
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }

object MovieService {
  private implicit val ec =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

  def getAll(value: String) = {
    val result = db.run(Movie.result)
    val test = Await.result(result, Duration.Inf)
    print(test)
  }

  def insertMovie(value: MovieRow) = {
  println("test")
    val insertMovie = Movie.returning(Movie) += value
    println("test1")

    val thing = Await.result(db.run(insertMovie), Duration.Inf)
    println("test2")

    println(thing)
    getAll("dd")
  }

  def updateMovieName(previous:String, name: String) = {
    val updatedMovie = Movie.filter(x => x.name === previous).map(_.name).update(name)
    Await.result(db.run(updatedMovie), Duration.Inf)
  }

  def sideEffect()={
    implicit val system = ActorSystem(Behaviors.empty, "SingleRequest")
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "http://akka.io"))

    responseFuture
      .onComplete {
        case Success(res) => println(res)
        case Failure(_) => sys.error("something wrong")
      }
  }
}
