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
      effectOnly(Effect(
        apiClient
          .login(sst.shared.Credentials(user, pass))
          .toFuture
          .map{
            case None => ErrorMsg("Login failed")
            case Some(session) => SetSession(session)
          }))
    case SetSession(session) =>
      logger.debug("Setting session")
      updated(Some(AuthToken(session)))
    case Register(user, pass) =>
      logger.debug("Setting session")
      effectOnly(Effect(
        apiClient
        .register(sst.shared.Credentials(user, pass))
          .toFuture
          .map{
            case Left(x) => ErrorMsg(s"Registration failed: ${x.error}")
            case Right(_) => SuccessMsg("Registration successful")
          }
      ))
    case ErrorMsg(msg) =>
      logger.error(msg) //TODO message box
      noChange
    case SuccessMsg(msg) =>
      logger.info(msg)
      noChange
  }

}
