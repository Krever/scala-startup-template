package sst.backend.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import endpoints.{Tupler, akkahttp}
import sst.backend.data._
import sst.backend.data.tables.{NoteEntity, NotebookEntity, NotebooksRepository, NotesRepository}
import sst.backend.util.SessionHelper
import sst.shared._

import scala.concurrent.ExecutionContext

class ApiRoutes(notesRepo: NotesRepository,
                notebooksRepo: NotebooksRepository,
                db: DBExecutor,
                sessionHelper: SessionHelper)(implicit ec: ExecutionContext)
  extends ApiEndpoints
    with akkahttp.server.Endpoints
    with akkahttp.server.CirceEntities {

  type RawSession[T] = T
  def sessionReqHeader: RequestHeaders[RawSession[Session]] = sessionHelper.requireSession

  def sessionSet[T](resp: Response[T])(implicit tupler: Tupler[T, RawSession[Session]]): Response[tupler.Out] =
    (out: tupler.Out) => {
      val (t, session) = tupler.unapply(out)
      sessionHelper.setSession(session){
        resp(t)
      }
    }

  override def authorized[T](response: (T) => Route): (Option[T]) => Route = {
      case Some(t) => response(t)
      case None => Directives.complete(StatusCodes.Forbidden)
    }


  import db.DBIOOps

  def routes: Route =
    getNotebooks.implementedByAsync { session: Session =>
      notebooksRepo
        .findAll()
        .run
        .map(x => Some(x.map(_.toDTO)))
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
      } ~
      login.implementedBy {
        c => Session(c.login)
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
