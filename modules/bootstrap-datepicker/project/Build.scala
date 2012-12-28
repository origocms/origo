import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo-bootstrap-datepicker"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
