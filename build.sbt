ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "order-processing-system",
    version := "0.1.0",

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
      "com.typesafe.akka" %% "akka-slf4j" % "2.8.5",
      "ch.qos.logback" % "logback-classic" % "1.4.11",
      "mysql" % "mysql-connector-j" % "8.3.0"
    )
  )