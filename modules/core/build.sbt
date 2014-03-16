import play.Project._

organization := "origo"

name := "core"

version := "0.1-SNAPSHOT"

resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("Objectify Play Repository - snapshots", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)

play.Project.playJavaSettings

libraryDependencies ++= Seq(
  javaCore,
  javaJdbc,
  javaJpa,
  "be.objectify" %% "deadbolt-java" % "2.2-RC4",
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.hibernate" % "hibernate-entitymanager" % "4.1.1.Final",
  "org.reflections" % "reflections" % "0.9.8",
  "org.springframework" % "spring-context" % "3.1.2.RELEASE",
  "org.jasypt" % "jasypt" %  "1.9.0",
  "org.jasypt" % "jasypt-hibernate4" %  "1.9.0"
)


