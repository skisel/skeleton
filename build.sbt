import play.PlayScala
import sbt.Keys._
import sbt._


name := "skeleton"

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3.0-2",
  "org.webjars" % "bootstrap" % "3.3.1",
  "org.webjars" % "requirejs" % "2.1.15",
  "org.webjars" % "bootbox" % "4.3.0",
  "org.webjars" % "angularjs" % "1.3.11",
  "org.webjars" % "angular-ui-bootstrap" % "0.12.0",
  "org.webjars" % "angular-ui-select" % "0.9.6",
  "org.webjars" % "angular-sanitize" % "1.3.11",
  "org.mockito" % "mockito-all" % "1.9.5",
  "org.specs2" %% "specs2" % "2.4.9" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.8" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.6" % "test"
)

scalaVersion := "2.11.4"

crossScalaVersions := Seq("2.11.4")

val main = Project("skeleton", file(".")).enablePlugins(PlayScala).settings(
  organization := "com.skisel.skeleton",
  version := "1.0",
  crossPaths := false,
  scalacOptions += "-language:postfixOps",
  scalaVersion := "2.11.4",
  crossScalaVersions := Seq("2.11.4"),
  javaOptions in run ++= Seq(
    "-XX:MaxPermSize=1024m",
    "-XX:-HeapDumpOnOutOfMemoryError",
    "-XX:HeapDumpPath=/data/logs",
    "-Xms13g",
    "-Xmx15g"
  ),
  Keys.fork in run := true
)

