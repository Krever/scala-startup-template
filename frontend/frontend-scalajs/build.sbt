lazy val `frontend-scalajs` = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(LocalProject("sharedJS"))
  .settings(
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1",
    libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "1.0.0-RC1"
    ,jsDependencies ++= Seq(
      "org.webjars.bower" % "react" % "15.4.2"
        /        "react-with-addons.js"
        minified "react-with-addons.min.js"
        commonJSName "React",

      "org.webjars.bower" % "react" % "15.4.2"
        /         "react-dom.js"
        minified  "react-dom.min.js"
        dependsOn "react-with-addons.js"
        commonJSName "ReactDOM",

      "org.webjars.bower" % "react" % "15.4.2"
        /         "react-dom-server.js"
        minified  "react-dom-server.min.js"
        dependsOn "react-dom.js"
        commonJSName "ReactDOMServer"/*,

      "org.webjars.bower" % "semantic-ui" % "2.2.9"
        /         "semantic.js"
        minified  "semantic.min.js"
        dependsOn "jquery.js",

      "org.webjars" % "jquery" % "2.1.3"
        /         "jquery.js"
        minified  "jquery.min.js"*/
    )
    ,persistLauncher := true
  )
  .settings(Linting.settings)
