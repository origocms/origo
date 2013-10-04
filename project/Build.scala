import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Origo"
  val appVersion      = "0.1-SNAPSHOT"

  val coreDependencies = Seq(
    // Built-Ins
    javaCore, javaJdbc,
    //"org.jadira.bindings" % "bindings" % "3.1.0.CR7",
    //"org.jadira.usertype" % "usertype.core" % "3.1.0.CR7",
    javaJpa, filters,
    // Extra
    "mysql" % "mysql-connector-java" % "5.1.18",
    "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final",
    "org.reflections" % "reflections" % "0.9.8",
    "be.objectify" %% "deadbolt-java" % "2.1-RC2",
    "org.jasypt" % "jasypt" %  "1.9.0",
    "org.jasypt" % "jasypt-hibernate4" %  "1.9.0"
  )

  /**
   * Core
   */
  val core = play.Project(
    appName + "-core", appVersion, coreDependencies, path = file("modules/core")
  ).settings(
    resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
  )

  /**
   * Authentication
   */
  val authenticationDependencies = Seq(
  ) ++ coreDependencies

  val authentication = play.Project(
    appName + "-authentication", appVersion, authenticationDependencies, path = file("modules/authentication")
  ).dependsOn( core ).aggregate( core )

  /**
   * Preview
   */
  val previewDependencies = Seq(
  ) ++ coreDependencies ++ authenticationDependencies

  val preview = play.Project(
    appName + "-preview", appVersion, previewDependencies, path = file("modules/preview")
  ).dependsOn( core, authentication ).aggregate( core, authentication )

  /**
   * Admin
   */
  val adminDependencies = Seq() ++ coreDependencies ++ authenticationDependencies

  val datepicker = play.Project(
    appName + "-bootstrap-datepicker", appVersion, adminDependencies, path = file("modules/bootstrap-datepicker")
  ).dependsOn( core ).aggregate( core )
  val bootstrap_wysihtml = play.Project(
    appName + "-bootstrap-wysihtml5", appVersion, adminDependencies, path = file("modules/bootstrap-wysihtml5")
  ).dependsOn( core ).aggregate( core )
  val admin = play.Project(
    appName + "-admin", appVersion, adminDependencies, path = file("modules/admin")
  ).dependsOn(
    core, preview, authentication, datepicker, bootstrap_wysihtml
  ).aggregate(
    core, preview, authentication, datepicker, bootstrap_wysihtml
  )

  /**
   * Bootstrap2 Theme
   */
  val bootstrap2themeDependencies = Seq(
  ) ++ coreDependencies

  val bootstrap2theme = play.Project(
    appName + "-theme-bootstrap2", appVersion, bootstrap2themeDependencies, path = file("themes/bootstrap2")
  ).dependsOn( core ).aggregate( core )

  /**
   * Bootstrap3 Theme
   */
  val bootstrap3themeDependencies = Seq(
  ) ++ coreDependencies

  val bootstrap3theme = play.Project(
    appName + "-theme-bootstrap3", appVersion, bootstrap2themeDependencies, path = file("themes/bootstrap3")
  ).dependsOn( core ).aggregate( core )

  /**
   * Main Application
   */
  val appDependencies = Seq() ++ coreDependencies ++ authenticationDependencies ++ adminDependencies

  val main = play.Project(
      appName, appVersion, appDependencies
  ).dependsOn(
      core, authentication, admin

    /** Start of non essential dependencies */
    , bootstrap2theme
    , bootstrap3theme
    /** End of non essential dependencies */

  ).aggregate(
      core, authentication, admin

  ).settings(
  )

}
