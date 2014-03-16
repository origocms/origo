name := "Origo Sample App"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
)

play.Project.playJavaSettings

// Modules (required)
lazy val core = project.in(file("modules/core"))
lazy val authentication = project.in(file("modules/authentication"))
lazy val admin = project.in(file("modules/admin"))
lazy val preview = project.in(file("modules/preview"))

// Modules (optional)
lazy val bootstrapDatePicker = project.in(file("modules/bootstrap-datepicker"))
lazy val boostrapWysiHtml5 = project.in(file("modules/bootstrap-wysihtml5"))

// Themes
lazy val bootstrap2 = project.in(file("themes/bootstrap2"))
lazy val bootstrap3 = project.in(file("themes/bootstrap3"))

lazy val main = project.in(file("."))
  .dependsOn(
    core, authentication, admin, preview,
    bootstrapDatePicker, boostrapWysiHtml5,
    bootstrap2, bootstrap3
  )
  .aggregate(
    core, authentication, admin, preview,
    bootstrapDatePicker, boostrapWysiHtml5,
    bootstrap2, bootstrap3
  )
