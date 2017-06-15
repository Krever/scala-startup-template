package sst.fronted.circuit

import diode.data.{Pot, Ready}
import diode.{ActionHandler, ActionResult, Effect, ModelRW}
import slogging.LazyLogging
import sst.fronted.service.ApiClient
import sst.shared.NotebookRequest

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by wpitula on 5/19/17.
  */
class NotebooksHandler[M](modelRW: ModelRW[M, (Pot[Notebooks], Option[AuthToken])], apiClient: ApiClient)
    extends ActionHandler(modelRW)
    with LazyLogging {
  override def handle: PartialFunction[Any, ActionResult[M]] = {
    case RefreshNotebooks =>
      effectOnly(
        Effect(
          apiClient
            .getNotebooks(value._2.get.jwtString)
            .toFuture
            .map(_.map(x => UpdateAllNotebooks(x.toSeq))
              .getOrElse(ErrorMsg("Getting notebooks failed: Unathorized")))))
    case UpdateAllNotebooks(notebooks) =>
      updatedNotebooks(Ready(Notebooks(notebooks)))
    case UpdateNotebook(notebook) =>
      def effect =
        apiClient.updateNotebook((notebook.id, NotebookRequest(notebook.name)))

      updatedNotebooks(value._1.map(_.updated(notebook)), Effect(effect.toFuture.map(_ => RefreshNotebooks)))
    case CreateNotebook(request) =>
      def effect = apiClient.createNotebook(request)

      effectOnly(Effect(effect.toFuture.map(_ => RefreshNotebooks)))
    case DeleteNotebook(notebook) =>
      updatedNotebooks(value._1.map(_.remove(notebook)),
                       Effect(
                         apiClient
                           .deleteNotebook(notebook.id)
                           .toFuture
                           .map(_ => RefreshNotebooks)))
  }

  private def updatedNotebooks(newValue: Pot[Notebooks]) = updated((newValue, value._2))

  private def updatedNotebooks(newValue: Pot[Notebooks], effect: Effect) = updated((newValue, value._2), effect)
}
