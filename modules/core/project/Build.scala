import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo-Core"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa,
    "org.springframework" % "spring-context" % "3.1.2.RELEASE"
  )

  val main = play.Project(
    appName, appVersion, appDependencies
  ).settings(
  )

}
