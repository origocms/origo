import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "Origo"
  val appVersion = "1.0-SNAPSHOT"
  val appOrganisation = "com.origocms"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := appOrganisation,
    version      := appVersion,
    javacOptions += "-Xlint:unchecked"
  )


  val appDeps = Seq(
    "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final"
  )
  
  val core = PlayProject(appName + "-core", appVersion, appDeps, path = file("modules/core"), mainLang = JAVA).settings(
    ebeanEnabled := false
    //libraryDependencies += appOrganisation % "origo-core-plugin" % appVersion
  ).dependsOn(
  )


//  val admin = PlayProject(appName + "-admin", appVersion, path = file("modules/admin"), mainLang = JAVA).settings(
//    ebeanEnabled := false
//  ).dependsOn(
//    core
//  )
//  val structuredContent = PlayProject(appName + "-structured-content", appVersion, path = file("modules/structured-content")).settings(
//    ebeanEnabled := true
//  ).dependsOn(
//    core
//  )
//
  val main = PlayProject(appName, appVersion, appDeps, mainLang = JAVA).settings(
    ebeanEnabled := false
  ).dependsOn(
    core
    //, admin, structuredContent
  )

}
