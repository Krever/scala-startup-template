package sst.fronted.circuit

import diode.{ActionHandler, ActionResult, Effect, ModelRW}
import diode.data.{Pot, Ready}
import slogging.LazyLogging
import sst.fronted.service.ApiClient
import sst.shared.NotebookRequest
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by wpitula on 5/19/17.
  */
class NotebooksHandler[M](modelRW: ModelRW[M, Pot[Notebooks]])
    extends ActionHandler(modelRW)
    with LazyLogging {
  override def handle: PartialFunction[Any, ActionResult[M]] = {
    case RefreshNotebooks =>
      effectOnly(
        Effect(
          ApiClient
            .getNotebooks(())
            .toFuture
            .map(x => UpdateAllNotebooks(x.toSeq))))
    case UpdateAllNotebooks(notebooks) =>
      updated(Ready(Notebooks(notebooks)))
    case UpdateNotebook(notebook) =>
      def effect =
        ApiClient.updateNotebook((notebook.id, NotebookRequest(notebook.name)))

      updated(value.map(_.updated(notebook)),
              Effect(effect.toFuture.map(_ => RefreshNotebooks)))
    case CreateNotebook(request) =>
      def effect = ApiClient.createNotebook(request)

      effectOnly(Effect(effect.toFuture.map(_ => RefreshNotebooks)))
    case DeleteNotebook(notebook) =>
      updated(value.map(_.remove(notebook)),
              Effect(
                ApiClient
                  .deleteNotebook(notebook.id)
                  .toFuture
                  .map(_ => RefreshNotebooks)))
  }
}
