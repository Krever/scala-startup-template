package sst.fronted.service

import endpoints.Tupler
import endpoints.algebra.CirceEntities.CirceCodec
import io.circe.parser
import org.scalajs.dom.raw.XMLHttpRequest
import slogging.LazyLogging
import sst.shared.Session

import scala.scalajs.js
import scala.scalajs.js.JSON
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object JWT extends js.Object {
  def read: js.Function1[String, js.Object] = js.native
}

case class JWTObject(claim: Claim)

case class Claim(data: Session)

class ApiClient()
  extends sst.shared.ApiEndpoints
    with endpoints.xhr.Endpoints
    with endpoints.xhr.thenable.Endpoints
    with endpoints.xhr.CirceEntities
    with LazyLogging {

  import io.circe.generic.auto._

  def readSession(jsonStr: String): Session = {
    logger.debug("Reading session from: " + jsonStr)
    val sessionJson = JSON.stringify(JWT.read(jsonStr))
    logger.debug(sessionJson)
    val json = parser.parse(sessionJson).right.get
    CirceCodec[JWTObject].decoder.decodeJson(json).right.get.claim.data
  }

  type RawSession[T] = String

  def sessionSet[T](resp: Response[T])(implicit tupler: Tupler[T, RawSession[Session]]): Response[tupler.Out] = {
    (xhr: XMLHttpRequest) => {
      resp(xhr).right.map { t =>
        tupler.apply(t, xhr.getResponseHeader(responseSessionHeaderName))
      }
    }
  }

  def sessionReqHeader: RequestHeaders[RawSession[Session]] =
    (s: RawSession[Session], req: XMLHttpRequest) =>
      req.setRequestHeader(requestSessionHeaderName, s)

  def authorized[T](response: js.Function1[XMLHttpRequest, Either[Exception, T]]): js.Function1[XMLHttpRequest, Either[Exception, Option[T]]] = {
    (xhr: XMLHttpRequest) => {
      if (xhr.status == 403) Right(None)
      else response(xhr).right.map(Some(_))
    }
  }

  def toUnit[T](x: T): Unit = logger.debug("To Unit: " + x.toString)

  override def validatedResponse[T](response: js.Function1[XMLHttpRequest, Either[Exception, T]])(implicit json: CirceCodec[BadRequest]): js.Function1[XMLHttpRequest, Either[Exception, Either[BadRequest, T]]] = {
    xhr => {
      val badRequest = jsonResponse[BadRequest]
      xhr.status match {
        case 400 => badRequest(xhr).right.map(Left(_))
        case _ => response(xhr).right.map(Right(_))
      }
    }
  }
}
