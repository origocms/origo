import play.Project._

organization := "origo.themes"

name := "bootstrap2"

version := "0.1-SNAPSHOT"

play.Project.playJavaSettings

libraryDependencies ++= Seq(
  javaCore,
  "origo" %% "core" % "0.1-SNAPSHOT"
)

lazy val core = project.in(file("../modules/core"))

lazy val bootstrap2 = project.in(file(".")) aggregate core dependsOn core
