import sbt._
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

  /**
   * Core and core dependencies
   */
  val core = play.Project(
    appName + "-core", appVersion, appDependencies, path = file("modules/core")
  )
  val structuredcontent = play.Project(
    appName + "-structuredcontent", appVersion, appDependencies, path = file("modules/structuredcontent")
  ).dependsOn( core ).aggregate( core )

  /**
   * Admin and admin dependencies
   */
  val datepicker = play.Project(
    appName + "-bootstrap-datepicker", appVersion, appDependencies, path = file("modules/bootstrap-datepicker")
  ).dependsOn( core ).aggregate( core )
  val bootstrap_wysihtml = play.Project(
    appName + "-bootstrap-wysihtml5", appVersion, appDependencies, path = file("modules/bootstrap-wysihtml5")
  ).dependsOn( core ).aggregate( core )
  val admin = play.Project(
    appName + "-admin", appVersion, appDependencies, path = file("modules/admin")
  ).dependsOn( core, datepicker, bootstrap_wysihtml ).aggregate( core, datepicker, bootstrap_wysihtml )

/*
    val adminArea = PlayProject(
      appName + "-admin", appVersion, path = file("modules/admin")
    ).dependsOn(core)
*/

  val main = play.Project(
      appName, appVersion, appDependencies
  ).dependsOn(
      core, admin, structuredcontent
  ).aggregate(
      core, admin, structuredcontent
  ).settings(
  )

}
