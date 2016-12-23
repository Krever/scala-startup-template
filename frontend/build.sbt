lazy val frontend = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    pipelineStages in Assets := Seq(scalaJSPipeline),
    scalaJSProjects := Seq(`frontend-scalajs`)
  )

lazy val `frontend-scalajs` = project
