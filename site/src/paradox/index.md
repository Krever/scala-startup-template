Scala Startup Template
=======

Scala startup template(SST) is  a project aiming to deliver e2e technology stack for creating typical web-based applications. 

## The stack

* Scala  programming language - for all application code
* SBT - for building the project
    * sbt-scalajs - for compiling scala.js
    * sbt-web-scalajs - for integrating scala.js with other web assets
    * sbt-native-packager - for packaging the application
    * sbt-scapegoat - for automated code linting
    * sbt-wartremover - for automated code linting
    * sbt-scalafmt - for automated code formatting
    * sbt-revolver - for fast development loop
    * sbt-updates - for finding stale dependencies
    * sbt-clippy - for more meaningful compile errors
    * sbt-site - for generating documentation
* Shared
    * endpoints - for defining API
    * endpoints-circe - for JSON serialization in API
* Backend
    * endpoints-akka-http-server - for serving the API
    * slick - for DB access
    * slick-repo - for less boilerplate in DB access
    * flyway - for DB migrations
    * sqlite - for data storage
    * slogging - for platform-agnostic logging abstraction
    * slf4j - for JVM logging abstraction
    * log4j - for logging
    * akka-http-session - for authentication
    * scala-bcrypt - for password encryption
* Frontend
    * scalajs-react/core - for scala react API
    * scalajs-react/extra - for routing
    * diode - for application control flow
    * diode-react" - for integration with scalajs-react
    * endpoints-xhr-client - for calling the API
    * slogging - for logging
    * scalajs-jquery - for jquery api
    * react - for view definition
    * semanticUI - for view styling
    * jquery - for interacting with semanticUI
* Other
    * .gitlab-ci.yml - for continuous integration
    * Procfile - for heroku deployment
    * jwilder/nginx-proxy - for reverse proxy
    * jrcs/letsencrypt-nginx-proxy-companion - for automated letencrypt-based https
    * bash - for automated deployment and running



