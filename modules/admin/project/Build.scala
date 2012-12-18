import sbt._
import Keys._
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

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
