// suport for compiling scalajs
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.4")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.15")

// suport for packaging application
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")

// code linting
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")
addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "0.3")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.0")

addSbtPlugin("com.softwaremill.clippy" % "plugin-sbt" % "0.5.3")