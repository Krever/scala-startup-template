package sst.backend.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import endpoints.akkahttp
import sst.backend.data._
import sst.backend.data.tables.{NoteEntity, NotebookEntity, NotebooksRepository, NotesRepository}
import sst.shared._

import scala.concurrent.ExecutionContext

class ApiRoutes(notesRepo: NotesRepository,
                notebooksRepo: NotebooksRepository,
                db: DBExecutor)(implicit ec: ExecutionContext)
  extends ApiEndpoints
    with akkahttp.server.Endpoints
    with akkahttp.server.CirceEntities {

  import db.DBIOOps

  def routes: Route =
    getNotebooks.implementedByAsync { _ =>
      notebooksRepo
        .findAll()
        .run
        .map(_.map(_.toDTO))
    } ~
      createNotebook.implementedByAsync { req =>
        notebooksRepo
          .save(req.toEntity)
          .run
          .map(_.toDTO)
      } ~
      deleteNotebook.implementedByAsync { id =>
        (for {
          nb <- notebooksRepo.findOne(id)
          d <- notebooksRepo.delete(nb.get)
        } yield ()).runTransactionally
      } ~
      updateNotebook.implementedByAsync {
        case (id, req) =>
          notebooksRepo
            .update(req.toEntity(id))
            .run
            .map(_=>())
      } ~
      getNotesFromNotebook.implementedByAsync { id =>
        notesRepo
          .findAllForNotebook(id)
          .run
          .map(_.map(_.toDTO))
      } ~
      createNote.implementedByAsync { req =>
        notesRepo
          .save(req.toEntity)
          .run
          .map(_ => ())
      } ~
      deleteNote.implementedByAsync { noteId =>
        (for {
          n <- notesRepo.findOne(noteId)
          d <- notesRepo.delete(n.get)
        } yield ()).runTransactionally
      } ~
      updateNote.implementedByAsync {
        case (id, req) =>
          notesRepo
            .update(req.toEntity(id))
            .run
            .map(_=>())
      }


  implicit class NotebookEntityOps(nb: NotebookEntity){
    def toDTO = Notebook(nb.id.get, nb.name)
  }
  implicit class NoteEntityOps(n: NoteEntity){
    def toDTO = Note(n.id.get, n.title, n.content, n.notebookId)
  }

  implicit class NotebookReqOps(nb: NotebookRequest){
    def toEntity = NotebookEntity(None, nb.name)
    def toEntity(id: Long) = NotebookEntity(Some(id), nb.name)
  }

  implicit class NoteReqOps(n: NoteRequest){
    def toEntity = NoteEntity(None, n.title,n.content, n.notebookId)
    def toEntity(id: Long) = NoteEntity(Some(id), n.title,n.content, n.notebookId)
  }
}
