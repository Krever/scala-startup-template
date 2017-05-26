#!/usr/bin/env bash

sbt '~frontend/assets' &

sbt '~backend/re-start ---
    -Dsst.backend.staticContent.serve=true
    -Dsst.backend.staticContent.paths.0=../frontend/target/web/public/main'

