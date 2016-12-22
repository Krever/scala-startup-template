
lazy val shared = crossProject.crossType(CrossType.Pure).in(file("."))

lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm