package sst.fronted.circuit

import diode.data.{Pot, Ready}
import diode.{ActionHandler, Effect, ModelRW}
import slogging.LazyLogging
import sst.fronted.service.ApiClient
import sst.shared.NoteRequest

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by wpitula on 5/19/17.
  */
class NotesHandler[M](modelRW: ModelRW[M, Pot[Notes]], apiClient: ApiClient)
    extends ActionHandler(modelRW)
    with LazyLogging {
  override def handle = {
    case RefreshNotes(notebookId) =>
      effectOnly(
        Effect(
          apiClient
            .getNotesFromNotebook(notebookId)
            .toFuture
            .map(x => UpdateAllNotes(notebookId, x.toSeq))))
    case UpdateAllNotes(_, notes) =>
      updated(Ready(Notes(notes)))
    case UpdateNote(note) =>
      def effect =
        apiClient.updateNote((note.id, NoteRequest(note.title, note.content, note.notebookId)))

      updated(value.map(_.updated(note)), Effect(effect.toFuture.map(_ => RefreshNotes(note.notebookId))))
    case CreateNote(request) =>
      def effect = apiClient.createNote(request)

      effectOnly(Effect(effect.toFuture.map(_ => RefreshNotes(request.notebookId))))
    case DeleteNote(note) =>
      updated(value.map(_.remove(note)),
              Effect(
                apiClient
                  .deleteNote(note.id)
                  .toFuture
                  .map(_ => RefreshNotes(note.notebookId))))
  }

}
