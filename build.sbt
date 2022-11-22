import slick.codegen.SourceCodeGenerator
import slick.model

lazy val akkaHttpVersion = "10.2.9"
lazy val akkaVersion = "2.6.19"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

scalaVersion := "2.13.8"
name := "My Akka HTTP Project"
organization := "com.rockthejvm"
version := "1.0"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe" % "config" % "1.4.2" from "https://repo1.maven.org/maven2/com/typesafe/config/1.4.2/config-1.4.2.jar",
  "mysql" %% "mysql-connector-java" % "8.0.27" from "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.27/mysql-connector-java-8.0.27.jar",
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.4" % Test,
  "org.postgresql" % "postgresql" % "42.3.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "com.github.tminglei" %% "slick-pg" % "0.20.3",
  "com.github.tminglei" %% "slick-pg_play-json" % "0.20.3"
)
val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

// https://mvnrepository.com/artifact/de.heikoseeberger/akka-http-circe
libraryDependencies += "de.heikoseeberger" %% "akka-http-circe" % "1.39.2"
resolvers ++= Seq("central" at "https://repo1.maven.org/maven2/")


//Slick Code Generation
slickCodegenSettings
enablePlugins(CodegenPlugin)
slickCodegenDatabaseUrl := "jdbc:postgresql://localhost:5432/postgres"
slickCodegenDatabaseUser := "postgres"
slickCodegenDatabasePassword := "admin"
slickCodegenDriver := slick.jdbc.PostgresProfile
slickCodegenJdbcDriver := "org.postgresql.Driver"
slickCodegenOutputPackage := "com.example.generated.models"
slickCodegenCodeGenerator := { (slickModel: model.Model) =>
  new SourceCodeGenerator(slickModel)
}
