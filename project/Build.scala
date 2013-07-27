import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
    // Built-Ins
    javaCore, javaJdbc, javaJpa, filters,
    // Extra
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final",
    "org.reflections" % "reflections" % "0.9.8",
    "be.objectify" %% "deadbolt-java" % "2.1-RC2"
  )

  /**
   * Core and core dependencies
   */
  val core = play.Project(
    appName + "-core", appVersion, appDependencies, path = file("modules/core")
  ).settings(
    resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
  )

  /**
   * Authentication
   */
  val authenticationDependencies = Seq(
    // Built-Ins
    javaCore, javaJdbc, javaJpa, filters,
    // Extra
    "org.jasypt" % "jasypt" %  "1.9.0",
    "org.jasypt" % "jasypt-hibernate4" %  "1.9.0"
  )
  val authentication = play.Project(
    appName + "-authentication", appVersion, authenticationDependencies, path = file("modules/authentication")
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
  ).dependsOn(
    core, authentication, datepicker, bootstrap_wysihtml
  ).aggregate(
    core, authentication, datepicker, bootstrap_wysihtml
  )

  /**
   * Structured Content
   */
  val structuredcontent = play.Project(
    appName + "-structuredcontent", appVersion, appDependencies, path = file("modules/structuredcontent")
  ).dependsOn( core, admin ).aggregate( core, admin )


  val main = play.Project(
      appName, appVersion, appDependencies
  ).dependsOn(
      core, authentication, admin, structuredcontent
  ).aggregate(
      core, authentication, admin, structuredcontent
  ).settings(
  )

}
