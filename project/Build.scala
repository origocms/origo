import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "Origo"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
    )

    val coreDependencies = Seq(
      "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final"
    )

    val core = PlayProject(appName + "-core", appVersion, coreDependencies, path = file("modules/core"), mainLang = JAVA).settings(
      ebeanEnabled := false
    )
    /*
    val admin = PlayProject(appName + "-admin", appVersion, path = file("modules/admin"), mainLang = JAVA).settings(
      ebeanEnabled := true
    ).dependsOn(
      core
    )
    val structuredContent = PlayProject(appName + "-structured-content", appVersion, path = file("modules/structured-content")).settings(
      ebeanEnabled := true
    ).dependsOn(
      core
    )
    */

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
        ebeanEnabled := false
    )
    .dependsOn(
        core
        //, admin, structuredContent
    )

}
