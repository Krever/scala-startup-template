lazy val backend = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .dependsOn(LocalProject("sharedJVM"))
  .settings(
    libraryDependencies ++= Seq(
      "org.julienrf" %% "endpoints-akka-http-server" % "0.2.0",
      "org.julienrf" %% "endpoints-akka-http-server-circe" % "0.2.0"
    ),
    Revolver.enableDebugging(port = 5050, suspend = false)
  )
  .settings(Linting.settings)
