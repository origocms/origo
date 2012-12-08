import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa,
    filters,
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.hibernate" % "hibernate-entitymanager" % "4.1.8.Final",
    "org.hibernate" % "hibernate-core" % "4.1.8.Final",
    "org.springframework" % "spring-context" % "3.1.2.RELEASE"
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
    appName, appVersion, appDependencies
  ).dependsOn(
      core
  ).settings(
  )

}
