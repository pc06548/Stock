name := "Stock"

version := "0.1"

scalaVersion := "2.12.10"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= 
  Seq(
    ws,
    guice,
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  )