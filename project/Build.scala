import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    "org.springframework" % "spring-context" % "3.1.2.RELEASE",
    javaCore,
    javaJdbc,
    javaEbean,
    filters
  )

  val core = play.Project(
      appName + "-core", appVersion, appDependencies, path = file("modules/core")
    )

/*
    val adminArea = PlayProject(
      appName + "-admin", appVersion, path = file("modules/admin")
    ).dependsOn(core)
*/

  val main = play.Project(
    appName, appVersion
  ).dependsOn(
    core
  ).settings(
    // Add your own project settings here      
  )

}
