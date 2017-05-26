lazy val frontend = (project in file("."))
  .aggregate(`frontend-scalajs`)
  .enablePlugins(SbtWeb)
  .enablePlugins(ScalafmtPlugin)
  .settings(
    pipelineStages in Assets := Seq(scalaJSPipeline),
    scalaJSProjects := Seq(`frontend-scalajs`),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.4.2",
      "org.webjars.bower" % "semantic-ui" % "2.2.9",
      "org.webjars" % "jquery" % "2.1.3"
    )
    ,devCommands in scalaJSPipeline ++= Seq("assets")
  )
  .settings(Linting.settings)

lazy val `frontend-scalajs` = project
