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
    "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final",
    "org.reflections" % "reflections" % "0.9.8",
    "org.springframework" % "spring-context" % "3.1.3.RELEASE"
  )

  val core = play.Project(
    appName + "-core", appVersion, appDependencies, path = file("modules/core")
  )
  val structuredcontent = play.Project(
    appName + "-structuredcontent", appVersion, appDependencies, path = file("modules/structuredcontent")
  ).dependsOn(
    core
  ).aggregate(
    core
  )

/*
    val adminArea = PlayProject(
      appName + "-admin", appVersion, path = file("modules/admin")
    ).dependsOn(core)
*/

  val main = play.Project(
      appName, appVersion, appDependencies
  ).dependsOn(
      core, structuredcontent
  ).aggregate(
      core, structuredcontent
  ).settings(
  )

}
