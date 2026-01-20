ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "order-processing-system",
    version := "0.1.0",

    Test / logBuffered := false,

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
      "com.typesafe.akka" %% "akka-slf4j" % "2.8.5",
      "ch.qos.logback" % "logback-classic" % "1.4.11",
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.8.5" % Test,
      "io.cucumber" %% "cucumber-scala" % "8.1.0" % Test,
      "io.cucumber" % "cucumber-junit" % "7.14.0" % Test
    )
  )