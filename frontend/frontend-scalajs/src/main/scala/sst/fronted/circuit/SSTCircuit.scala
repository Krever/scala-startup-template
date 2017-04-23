package sst.fronted.circuit

import diode._
import diode.data._
import diode.react.ReactConnector
import sst.fronted.service.ApiClient
import sst.shared.{Notebook, NotebookRequest}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import js.Dynamic.{ global => g }

// The base model of our application
case class RootModel(notebooks: Pot[Notebooks]/*, note: Pot[Notes]*/)

case class Notebooks(items: Seq[Notebook]) {
  def updated(newItem: Notebook): Notebooks = {
    items.indexWhere(_.id == newItem.id) match {
      case -1 =>
        Notebooks(items :+ newItem)
      case idx =>
        Notebooks(items.updated(idx, newItem))
    }
  }

  def remove(item: Notebook) = Notebooks(items.filterNot(_ == item))
}

//case class Notes(items: Map[Long, Seq[Note]]) {
//
//  def updated(notebookId: Long, updateFunc: Seq[Note] => Seq[Note]): Notes = {
//    val notes = items.getOrElse(notebookId, Seq())
//    Notes(items.updated(notebookId, updateFunc(notes)))
//  }
//
//  def updated(newItem: Note): Notes = {
//    updated(newItem.notebookId, notes =>
//      notes.indexWhere(_.id == newItem.id) match {
//      case -1 =>
//        notes :+ newItem
//      case idx =>
//        notes.updated(idx, newItem)
//    })
//  }
//
//  def remove(item: Note): Notes = updated(item.notebookId, notes => notes.filterNot(_.id == item.id))
//}


class NotebooksHandler[M](modelRW: ModelRW[M, Pot[Notebooks]]) extends ActionHandler(modelRW) {
  override def handle = {
    case x =>
      g.console.log(s"Handling $x")
      x match {
      case RefreshNotebooks =>
        effectOnly(Effect(ApiClient.getNotebooks(()).toFuture.map(x => UpdateAllNotebooks(x.toSeq))))
      case UpdateAllNotebooks(notebooks) =>
        updated(Ready(Notebooks(notebooks)))
      case CreateOrUpdateNotebook(notebook) =>
        val effect = if (notebook.id == -1)
          ApiClient.createNotebook(NotebookRequest(notebook.name))
        else
          ApiClient.updateNotebook((notebook.id, NotebookRequest(notebook.name)))
        updated(value.map(_.updated(notebook)), Effect(effect.toFuture.map(_ => RefreshNotebooks)))
      case DeleteNotebook(notebook) =>
        updated(value.map(_.updated(notebook)), Effect(ApiClient.deleteNotebook(notebook.id).toFuture.map(_ => RefreshNotebooks)))

      //    case UpdateTodo(item) =>
      //      // make a local update and inform server
      //      updated(value.map(_.updated(item)), Effect(AjaxClient[Api].updateTodo(item).call().map(UpdateAllTodos)))
      //    case DeleteTodo(item) =>
      //      // make a local update and inform server
      //      updated(value.map(_.remove(item)), Effect(AjaxClient[Api].deleteTodo(item.id).call().map(UpdateAllTodos)))
    }
  }
}

// Application circuit
object SSTCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  // combine all handlers into one
  override protected val actionHandler = composeHandlers(
    new NotebooksHandler(zoomRW(_.notebooks)((m, v) => m.copy(notebooks = v)))
//    ,new MotdHandler(zoomRW(_.motd)((m, v) => m.copy(motd = v)))
  )

  // initial application model
  override protected def initialModel = RootModel(Empty/*, /*Empty*/*/)
}