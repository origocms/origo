import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo-Core"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    "org.springframework" % "spring-context" % "3.1.2.RELEASE",
    javaCore,
    javaJdbc,
    javaJpa
  )

  val main = play.Project(
    appName, appVersion, appDependencies
  ).settings(
  )

}
