
lazy val backend = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .dependsOn(LocalProject("sharedJVM"))
