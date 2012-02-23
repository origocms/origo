import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Origo"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val core = PlayProject(appName + "-core", appVersion, path = file("modules/core"), mainLang = JAVA)
    val admin = PlayProject(appName + "-admin", appVersion, path = file("modules/admin"), mainLang = JAVA)
    val structuredContent = PlayProject(appName + "-structured-content", appVersion, path = file("modules/structured-content"))

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
        ebeanEnabled := true
    )
    .dependsOn(
        core
        //, admin, structuredContent
    )

}
