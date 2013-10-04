import sbt._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "origo.themes.bootstrap2"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
