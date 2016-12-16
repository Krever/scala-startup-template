
lazy val backend = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .dependsOn(sharedJvm)

lazy val sharedJvm = crossProject.crossType(CrossType.Pure).in(file("../shared")).jvm