
lazy val `frontend-scalajs` = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)
  .settings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    persistLauncher := true
  )

lazy val sharedJs = crossProject.crossType(CrossType.Pure).in(file("../../shared")).js