import wartremover.Warts

lazy val root = (project in file("."))
  .aggregate(frontend, backend)
  .dependsOn(frontend, backend)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "scala-startup-template",
    version in ThisBuild := "0.1.0",
    scalaVersion in ThisBuild := "2.12.2",
    mainClass in Compile := Some("sst.backend.Main")
  )
  .settings(Linting.settings)

lazy val backend = project

lazy val frontend = project

//Shared project config
lazy val shared = crossProject
  .crossType(CrossType.Pure)
  .settings(Linting.settings)
  .settings(
    resolvers +=  Resolver.mavenLocal,
    libraryDependencies ++= Seq(
      "org.julienrf" %% "endpoints-algebra" % "0.2.0-SNAPSHOT-1",
      "org.julienrf" %% "endpoints-algebra-circe" % "0.2.0-SNAPSHOT-1"
    )
  )

lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm
