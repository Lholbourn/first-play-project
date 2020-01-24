name := """first-project"""
organization := "tna"

version := "1.0-SNAPSHOT"

val circeVersion = "0.12.3"
lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.4"
libraryDependencies += ws
libraryDependencies += ehcache
libraryDependencies ++= Seq(
ws
)
libraryDependencies += "com.softwaremill.sttp" %% "core" % "1.7.2"
libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "tna.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "tna.binders._"
