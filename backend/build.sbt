lazy val backend = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .dependsOn(LocalProject("sharedJVM"))
  .settings(
    libraryDependencies ++= Seq(
      "org.julienrf" %% "endpoints-akka-http-server" % "0.2.0-SNAPSHOT-1" changing(),
      "org.julienrf" %% "endpoints-akka-http-server-circe" % "0.2.0-SNAPSHOT-1" changing()
    ),
    Revolver.enableDebugging(port = 5050, suspend = false)
  )
  .settings(Linting.settings)
