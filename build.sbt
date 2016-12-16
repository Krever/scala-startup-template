lazy val root = (project in file("."))
  .dependsOn(frontend, backend)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-startup-template",
    version in ThisBuild := "0.1.0",
    scalaVersion in ThisBuild := "2.12.1",
    scalacOptions in ThisBuild := Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused",
      "-Ywarn-unused-import"),
    mainClass in Compile := Some("sst.backend.Main")
  )

lazy val backend = project

lazy val frontend = project

lazy val shared = crossProject.crossType(CrossType.Pure)