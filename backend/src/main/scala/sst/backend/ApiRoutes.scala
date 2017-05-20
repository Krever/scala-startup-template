package sst.backend

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import endpoints.akkahttp
import sst.shared.{ApiEndpoints, Note, Notebook}

import scala.util.Random

object ApiRoutes
    extends ApiEndpoints
    with akkahttp.server.Endpoints
    with akkahttp.server.CirceEntities {

  private var notebooks = (1L to 6L)
    .map(i => Notebook(randomId(), s"Notebook $i"))
    .map(nb => (nb.id, nb))
    .toMap
  private var notes = notebooks.values
    .flatMap(n =>
      (1L to 6L).map(i =>
        Note(randomId(), s"Note $i", s"Note $i content", n.id)))
    .map(n => (n.id, n))
    .toMap

  def routes: Route =
    getNotebooks.implementedBy { _ =>
      notebooks.values.toList.sortBy(_.name)
    } ~
      createNotebook.implementedBy { req =>
        val id = randomId()
        val notebook = Notebook(id, req.name)
        notebooks += ((id, notebook))
        notebook
      } ~
      deleteNotebook.implementedBy { id =>
        notebooks -= id
      } ~
      updateNotebook.implementedBy {
        case (id, req) =>
          notebooks -= id
          val notebook = Notebook(id, req.name)
          notebooks += ((id, notebook))
      } ~
      getNotesFromNotebook.implementedBy { id =>
        notes.values.filter(_.notebookId == id)
      } ~
      createNote.implementedBy { req =>
        val id = randomId()
        notes += ((id, Note(id, req.title, req.content, req.notebookId)))
      } ~
      deleteNote.implementedBy { noteId =>
        notes -= noteId
      } ~
      updateNote.implementedBy {
        case (noteId, req) =>
          val updatedNote =
            notes(noteId).copy(title = req.title, content = req.content)
          notes = notes.updated(noteId, updatedNote)
      }

  private def randomId(): Long = Math.abs(Random.nextInt().toLong)
}
