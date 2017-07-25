package sst.shared

import endpoints.algebra.CirceEntities
import endpoints.{Tupler, algebra}

/**
  * Created by wpitula on 3/11/17.
  */
trait ApiBase
  extends algebra.Endpoints
    with algebra.JsonEntities
    with CirceEntities {

  def apiBasePath: Path[Unit]

  def requestSessionHeaderName = "Authorization"

  def responseSessionHeaderName = "Set-Authorization"

  type RawSession[T]

  def sessionReqHeader: RequestHeaders[RawSession[Session]]

  def sessionSet[T](resp: Response[T])(implicit tupler: Tupler[T, RawSession[Session]]): Response[tupler.Out]

  def authorized[T](response: Response[T]): Response[Option[T]]

  def sessionSecuredEndpoint[A, B, C, AB](
    method: Method,
    url: Url[A],
    requestEntity: RequestEntity[B] = emptyRequest,
    response: Response[C]
  )(implicit
    tuplerAB: Tupler.Aux[A, B, AB],
    tuplerABC: Tupler[AB, RawSession[Session]]
  ): Endpoint[tuplerABC.Out, Option[C]] =
    endpoint(request(method, url, requestEntity, sessionReqHeader), authorized(response))


  case class BadRequest(error: String)

  def validatedResponse[T](response: Response[T])(implicit json: JsonResponse[BadRequest]): Response[Either[BadRequest, T]]

}