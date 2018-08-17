name := """poc-play-zipkin"""
organization := "org.talend"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.13"
libraryDependencies += ws
libraryDependencies += "jp.co.bizreach" %% "play-zipkin-tracing-play" % "2.1.0"
libraryDependencies += "jp.co.bizreach" %% "play-zipkin-tracing-akka" % "2.1.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.talend.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.talend.binders._"
