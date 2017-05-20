lazy val root = (project in file("."))
  .aggregate(frontend, backend)
  .dependsOn(frontend, backend)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .enablePlugins(ScalafmtPlugin)
  .settings(
    name := "scala-startup-template",
    version in ThisBuild := "0.1.0",
    scalaVersion in ThisBuild := "2.12.2",
    mainClass in Compile := Some("sst.backend.Main"),
    mappings in Universal <++= (WebKeys.stage in frontend) map { dir =>
      ((dir.*** --- dir) pair relativeTo(dir)).map(x => (x._1, "webstage/"+x._2))
    },
    scriptClasspath in bashScriptDefines ~= (cp => "../webstage" +: cp),
    dockerRepository in Docker := Some("registry.gitlab.com/w-pitula"),
    dockerUpdateLatest in Docker := true,
    aggregate in Docker := false
  )
  .settings(Linting.settings)

lazy val backend = project

lazy val frontend = project

//Shared project config
lazy val shared = crossProject
  .crossType(CrossType.Pure)
  .enablePlugins(ScalafmtPlugin)
  .settings(Linting.settings)
  .settings(
    resolvers +=  Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "org.julienrf" %% "endpoints-algebra" % "0.2.0",
      "org.julienrf" %% "endpoints-algebra-circe" % "0.2.0"
    )
  )

lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm
