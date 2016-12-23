lazy val frontend = (project in file("."))
  .enablePlugins(SbtWeb)
  .settings(
    pipelineStages in Assets := Seq(scalaJSPipeline),
    scalaJSProjects := Seq(`frontend-scalajs`)
  )
  .settings(Linting.settings)

lazy val `frontend-scalajs` = project
