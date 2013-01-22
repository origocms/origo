import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo-Admin"
  val appVersion      = "0.2-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaJPA
  )

  val datepicker = play.Project(
    appName + "-bootstrap-datepicker", appVersion, appDependencies, path = file("modules/bootstrap-datepicker")
  )

  val main = play.Project(appName, appVersion, appDependencies).dependsOn(
    datepicker
  ).aggregate(
    datepicker
  ).settings(
  )

}
