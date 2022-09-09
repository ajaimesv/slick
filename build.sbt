ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ywarn-dead-code",
  "-Xfatal-warnings"
)

lazy val root = (project in file("."))
  .settings(
    name := "slick",
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick"           % "3.3.3",
      "com.h2database"      % "h2"              % "2.1.214",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "ch.qos.logback"      % "logback-classic" % "1.4.0"
    )
  )
