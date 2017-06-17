lazy val site = (project in file("."))
  .enablePlugins(ParadoxSitePlugin)
  .enablePlugins(GhpagesPlugin)
  .settings(
    git.remoteRepo := "git@github.com:Krever/scala-startup-template.git"
  )
  .settings(Linting.settings)

