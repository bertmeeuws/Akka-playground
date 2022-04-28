package com.example.utils

import com.typesafe.config.ConfigFactory

trait Config {
    //load application.conf
    private val config = ConfigFactory.load()
    private val databaseConfig = config.getConfig("database")

    val databaseUrl = databaseConfig.getString("url")
    val databaseUser = databaseConfig.getString("user")
    val databasePassword = databaseConfig.getString("password")
}