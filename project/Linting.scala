import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import sbt.Keys._
import sbt._
import wartremover.WartRemover.autoImport._
import wartremover.Warts


object Linting {
  val settings = Seq(
    scalacOptions in ThisBuild ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused",
      "-Ywarn-unused-import"),
    scapegoatVersion := "1.3.0",
    compile in Compile := (compile in Compile dependsOn scapegoat).value,
    wartremoverErrors ++= Warts.unsafe
  )
}