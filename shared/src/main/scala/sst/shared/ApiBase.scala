package sst.shared

import endpoints.algebra.CirceEntities
import endpoints.{Tupler, algebra}

/**
  * Created by wpitula on 3/11/17.
  */
trait ApiBase
  extends algebra.Endpoints
    with CirceEntities
    with algebra.Builders
    with algebra.JsonBuilders {

  def apiBasePath: Path[Unit]


  def requestSessionHeaderName = "Authorization"

  def responseSessionHeaderName = "Set-Authorization"

  type RawSession[T]

  def sessionReqHeader: RequestHeaders[RawSession[Session]]

  def sessionSet[T](resp: Response[T])(implicit tupler: Tupler[T, RawSession[Session]]): Response[tupler.Out]

  def authorized[T](response: Response[T]): Response[Option[T]]

  implicit class SessionEndpointOps[U, ReqH, ReqE, RespE](endp: EndpointBuilder[U, ReqH, ReqE, RespE]) {
    def buildSessionSecured[UE, NewH](implicit
                                      tupler: Tupler.Aux[ReqH, RawSession[Session], NewH],
                                      tuplerUE: Tupler.Aux[U, ReqE, UE],
                                      tuplerUEH: Tupler[UE, NewH]): Endpoint[tuplerUEH.Out, Option[RespE]] = {
      val resp = authorized(endp.response)
      endp
        .addRequestHeaders(sessionReqHeader)
        .withResponse(resp)
        .build[UE]
    }
  }

  case class BadRequest(error: String)

  def validatedResponse[T](response: Response[T])(implicit json: JsonResponse[BadRequest]): Response[Either[BadRequest, T]]

}