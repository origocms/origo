import play.Project._

organization := "origo"

name := "preview"

version := "0.1-SNAPSHOT"

play.Project.playJavaSettings

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaJpa,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final",
  "origo" %% "core" % "0.1-SNAPSHOT"
)

lazy val core = project in file (".")

lazy val main = project aggregate core dependsOn core

