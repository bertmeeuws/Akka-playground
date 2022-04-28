package com.example.dao

import com.example.utils.{DatabaseConfig}
import slick.sql.SqlAction
import slick.dbio.NoStream
import slick.sql.FixedSqlStreamingAction
import scala.concurrent.Future

trait BaseDAO extends DatabaseConfig {


    protected implicit def executeFromDb[A](action: SqlAction[A, NoStream, _ <: slick.dbio.Effect]): Future[A] = {
    db.run(action)
  }
  
  protected implicit def executeReadStreamFromDb[A](action: FixedSqlStreamingAction[Seq[A], A, _ <: slick.dbio.Effect]): Future[Seq[A]] = {
    db.run(action)
  }
}