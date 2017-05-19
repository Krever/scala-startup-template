package sst.backend

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory

/**
  * Created by wpitula on 3/11/17.
  */
object FrontedRoutes {

  import akka.http.scaladsl.server.Directives._

  val uiPrefix = ""
  import scala.collection.JavaConverters._

  private val config = ConfigFactory.load().getConfig("sst.backend.staticContent")
  private val resourcePaths = config.getStringList("resourcePaths").asScala
  private val paths = config.getStringList("paths").asScala

  private val contentAccessRoute = resourcePaths
    .map(path => getFromResourceDirectory(path))
    .fold(reject)(_ ~ _) ~ paths
    .map(path => getFromDirectory(path))
    .fold(reject)(_ ~ _)

  val routes: Route = (get & pathPrefix(uiPrefix)) {
    pathEndOrSingleSlash {
      redirect("index.html", StatusCodes.PermanentRedirect)
    } ~
      contentAccessRoute
  }

}
