import play.Project._

name := "bootstrap-datepicker"

organization := "origo"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  "origo" %% "core" % "0.1-SNAPSHOT"
)

lazy val core = project.in(file("modules/core"))

play.Project.playJavaSettings

lazy val main = project.in(file("."))
  .dependsOn(core)
  .aggregate(core)

