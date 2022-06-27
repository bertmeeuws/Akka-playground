lazy val akkaHttpVersion = "10.2.9"
lazy val akkaVersion    = "2.6.19"

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
    )),
    name := "My Akka HTTP Project",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",
      "com.typesafe.slick" %% "slick" % "3.3.3",

      "com.typesafe" % "config" % "1.4.2" from "https://repo1.maven.org/maven2/com/typesafe/config/1.4.2/config-1.4.2.jar",
      "mysql" %% "mysql-connector-java" % "8.0.27" from "https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.27/mysql-connector-java-8.0.27.jar",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.1.4"         % Test,
      "org.scalameta" %% "sbt-scalafmt" % "2.4.6"
    ),
    resolvers ++= Seq("central" at "https://repo1.maven.org/maven2/")
  )
