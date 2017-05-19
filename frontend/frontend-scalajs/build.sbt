lazy val `frontend-scalajs` = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(LocalProject("sharedJS"))
  .settings(
    resolvers += Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.github.japgolly.scalajs-react" %%% "core" % "1.0.0-RC2",
      "com.github.japgolly.scalajs-react" %%% "extra" % "1.0.0-RC2",
      "io.suzaku" %%% "diode" % "1.1.2-SNAPSHOT",
      "io.suzaku" %%% "diode-react" % "1.1.2-SNAPSHOT",
      "org.julienrf" %%% "endpoints-xhr-client" % "0.2.0-SNAPSHOT-1",
      "org.julienrf" %%% "endpoints-xhr-client-circe" % "0.2.0-SNAPSHOT-1",
      "biz.enef" %%% "slogging" % "0.5.2",
      "be.doeraene" %%% "scalajs-jquery" % "0.9.1"
    ),
    scalaJSUseMainModuleInitializer := true
  )
  .settings(Linting.settings)
