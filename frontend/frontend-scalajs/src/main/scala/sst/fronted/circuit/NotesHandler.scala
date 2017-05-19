package sst.fronted.circuit

import diode.{ActionHandler, Effect, ModelRW}
import diode.data.{Pot, Ready}
import sst.fronted.service.ApiClient
import sst.shared.NoteRequest
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by wpitula on 5/19/17.
  */
class NotesHandler[M](modelRW: ModelRW[M, Pot[Notes]]) extends ActionHandler(modelRW) {
  override def handle = {
    case RefreshNotes(notebookId) =>
      effectOnly(Effect(ApiClient.getNotesFromNotebook(notebookId).toFuture.map(x => UpdateAllNotes(notebookId, x.toSeq))))
    case UpdateAllNotes(_, notes) =>
      updated(Ready(Notes(notes)))
    case UpdateNote(note) =>
      def effect = ApiClient.updateNote((note.id, NoteRequest(note.title, note.content, note.notebookId)))

      updated(value.map(_.updated(note)), Effect(effect.toFuture.map(_ => RefreshNotes(note.notebookId))))
    case CreateNote(request) =>
      def effect = ApiClient.createNote(request)

      effectOnly(Effect(effect.toFuture.map(_ => RefreshNotes(request.notebookId))))
    case DeleteNote(note) =>
      updated(value.map(_.remove(note)), Effect(ApiClient.deleteNote(note.id).toFuture.map(_ => RefreshNotes(note.notebookId))))
  }

}
