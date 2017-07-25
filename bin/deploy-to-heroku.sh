#!/usr/bin/env bash

sbt stage
git push heroku master
heroku restart