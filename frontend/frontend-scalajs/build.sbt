lazy val `frontend-scalajs` = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(LocalProject("sharedJS"))
  .settings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    persistLauncher := true
  )
