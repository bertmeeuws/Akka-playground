package com.example.dao

import PostgresProfile.profile.api._

object Connection {
  val db = Database.forConfig("postgres")
}
