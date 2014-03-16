import play.Project._

name := "admin"

organization := "origo"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  javaCore,
  "origo" %% "core" % "0.1-SNAPSHOT",
  "origo" %% "authentication" % "0.1-SNAPSHOT"
)

lazy val core = project.in(file("modules/core"))
lazy val auth = project.in(file("modules/authentication"))

play.Project.playJavaSettings

lazy val main = project.in(file("."))
  .dependsOn(core, auth)
  .aggregate(core, auth)

