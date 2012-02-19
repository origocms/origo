import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Origo"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
    )

    val core = PlayProject(appName + "-core", appVersion, path = file("modules/core"))
    val admin = PlayProject(appName + "-admin", appVersion, path = file("modules/admin"))
    val structuredContent = PlayProject(appName + "-structured-content", appVersion, path = file("modules/structured-content"))

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(

    )
    .dependsOn(
        core, admin, structuredContent
    )

}
