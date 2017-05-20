#!/usr/bin/env bash

#Optional, uncomment if you are modifying dependencies or index.html
sbt 'frontend/webStage'

sbt '~backend/re-start ---
    -Dsst.backend.staticContent.serve=true
    -Dsst.backend.staticContent.paths.0=../frontend/target/web/stage
    -Dsst.backend.staticContent.paths.1=../frontend/frontend-scalajs/target/scala-2.12' &

sbt '~frontend-scalajs/fastOptJS'
