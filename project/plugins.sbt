// suport for compiling scalajs in sbt-web project
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.3")
// support for compiling scalajs
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.15")
// suport for packaging application
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.1.4")
// code linting
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")
addSbtPlugin("org.wartremover" % "sbt-wartremover" % "1.2.1")
addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.5.0")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")