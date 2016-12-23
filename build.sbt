lazy val root = (project in file("."))
  .aggregate(frontend, backend)
  .dependsOn(frontend, backend)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-startup-template",
    version in ThisBuild := "0.1.0",
    scalaVersion in ThisBuild := "2.12.1",
    mainClass in Compile := Some("sst.backend.Main")
  )

lazy val backend = project

lazy val frontend = project

lazy val shared = crossProject.crossType(CrossType.Pure)
lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm