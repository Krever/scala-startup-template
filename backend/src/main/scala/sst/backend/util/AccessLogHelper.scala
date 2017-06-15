package sst.backend.util

import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.{Directive0, RouteResult}
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LoggingMagnet}
import slogging.LazyLogging

/**
  * Created by wpitula on 6/11/17.
  */
object AccessLogHelper extends LazyLogging {

  def logAccess: Directive0 =
    DebuggingDirectives.logRequestResult(LoggingMagnet(_ => logRequestResponse))

  private def logRequestResponse(req: HttpRequest)(res: RouteResult): Unit =
    logger.debug(s"""
         |Request: $req
         |Response: $res
         |""".stripMargin)

}
