package com.example.dao.MovieService

import java.util.concurrent.Executors

import com.example.dao.Connection
import com.example.models.codegen.Tables
import com.example.models.codegen.Tables._
import com.example.models.codegen.Tables.profile.api._
import scala.util.{Success, Failure, Try}

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import slick.lifted

object MovieService {
  private implicit val ec =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))

  def getAll() = {
    Connection.db.run(Movie.result)
  }
}
