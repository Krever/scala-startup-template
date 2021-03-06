lazy val `frontend-scalajs` = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .enablePlugins(ScalafmtPlugin)
  .dependsOn(LocalProject("sharedJS"))
  .settings(
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % "1.0.0",
      "com.github.japgolly.scalajs-react" %%% "extra" % "1.0.0",
      "io.suzaku" %%% "diode" % "1.1.2",
      "io.suzaku" %%% "diode-react" % "1.1.2",
      "org.julienrf" %%% "endpoints-xhr-client" % "0.3.0",
      "org.julienrf" %%% "endpoints-xhr-client-circe" % "0.3.0",
      "biz.enef" %%% "slogging" % "0.5.2",
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
    ),
    scalaJSUseMainModuleInitializer := true
  )
  .settings(Linting.settings)
