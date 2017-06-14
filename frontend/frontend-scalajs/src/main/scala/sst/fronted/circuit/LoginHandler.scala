package sst.fronted.circuit

import diode.{ActionHandler, Effect, ModelRW}
import slogging.LazyLogging
import sst.fronted.service.ApiClient

import scala.concurrent.ExecutionContext.Implicits.global

class LoginHandler[M](modelRW: ModelRW[M, Option[AuthToken]], apiClient: ApiClient)
    extends ActionHandler(modelRW) with LazyLogging {
  override def handle = {
    case Login(user, pass) =>
      logger.debug("Handling login")
      effectOnly(Effect(apiClient.login(sst.shared.Credentials(user, pass)).toFuture.map(x => SetSession(x))))
    case SetSession(session) =>
      logger.debug("Setting session")
      updated(Some(AuthToken(session)))
  }

}
