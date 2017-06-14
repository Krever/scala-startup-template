lazy val backend = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(ScalafmtPlugin)
  .dependsOn(LocalProject("sharedJVM"))
  .settings(
    libraryDependencies ++= Seq(
      "org.julienrf" %% "endpoints-akka-http-server" % "0.3.0-1-SNAPSHOT",
      "org.julienrf" %% "endpoints-akka-http-server-circe" % "0.3.0-1-SNAPSHOT",
      "com.typesafe.slick" %% "slick" % "3.2.0",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
      "com.byteslounge" %% "slick-repo" % "1.4.3",
      "org.flywaydb" % "flyway-core" % "4.2.0",
      "org.xerial" % "sqlite-jdbc" % "3.18.0",
      "biz.enef" %% "slogging" % "0.5.2",
      "biz.enef" %% "slogging-slf4j" % "0.5.2",
      "org.slf4j" % "slf4j-log4j12" % "1.7.25",
      "com.softwaremill.akka-http-session" %% "core" % "0.4.0",
      "com.softwaremill.akka-http-session" %% "jwt"  % "0.4.0"
    ),
    Revolver.enableDebugging(port = 5050, suspend = false)
  )
  .settings(Linting.settings)
