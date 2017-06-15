package sst.fronted.circuit

import diode.{ActionHandler, ModelRW}
import slogging.LazyLogging

class MessagesHandler[M](modelRW: ModelRW[M, Option[Message]]) extends ActionHandler(modelRW) with LazyLogging {
  override def handle = {
    case ErrorMsg(msg) =>
      logger.error(msg)
      updated(Some(Message(msg, MessageType.Error)))
    case SuccessMsg(msg) =>
      logger.info(msg)
      updated(Some(Message(msg, MessageType.Success)))
    case CloseMsg =>
      updated(None)
  }

}
