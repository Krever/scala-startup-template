package sst.backend.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpcirce.CirceSupport
import endpoints.algebra.CirceEntities
import endpoints.{Tupler, akkahttp}
import slick.dbio.DBIO
import sst.backend.data._
import sst.backend.data.tables._
import sst.backend.util.SessionHelper
import sst.shared._

import scala.concurrent.ExecutionContext

class ApiRoutes(notesRepo: NotesRepository,
                notebooksRepo: NotebooksRepository,
                usersRepo: UsersRepository,
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
      sessionHelper.setSession(session) {
        resp(t)
      }
    }

  override def authorized[T](response: (T) => Route): (Option[T]) => Route = {
    case Some(t) => response(t)
    case None => Directives.complete(StatusCodes.Forbidden)
  }

  override def validatedResponse[T](response: (T) => Route)(
      implicit json: CirceEntities.CirceCodec[BadRequest]): (Either[BadRequest, T]) => Route = {
    {
      case Left(x) =>
        implicit val encoder = json.encoder
        import CirceSupport._
        Directives.complete((StatusCodes.BadRequest, x))
      case Right(x) => response(x)
    }
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
            .map(_ => ())
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
            .map(_ => ())
      } ~
      login.implementedByAsync { cred =>
        {
          import com.github.t3hnar.bcrypt._
          usersRepo
            .findByUsername(cred.login)
            .run
            .map {
              case Some(u) if u.passwordHash == cred.password.bcrypt(u.salt) =>
                Some(Session(u.username))
              case None =>
                None
            }
        }
      } ~
      register.implementedByAsync(
        cred => {
          usersRepo
            .findByUsername(cred.login)
            .flatMap { userOpt =>
              userOpt
                .map(x => DBIO.successful(Left(BadRequest("Username taken"))))
                .getOrElse {
                  import com.github.t3hnar.bcrypt._
                  val salt: String = generateSalt
                  val passwordHash: String = cred.password.bcrypt(salt)
                  val user = UserEntity(None, cred.login, passwordHash, salt)
                  usersRepo.save(user).map(_ => Right(()))
                }
            }
            .runTransactionally
        }
      )

  implicit class NotebookEntityOps(nb: NotebookEntity) {
    def toDTO = Notebook(nb.id.get, nb.name)
  }
  implicit class NoteEntityOps(n: NoteEntity) {
    def toDTO = Note(n.id.get, n.title, n.content, n.notebookId)
  }

  implicit class NotebookReqOps(nb: NotebookRequest) {
    def toEntity = NotebookEntity(None, nb.name)
    def toEntity(id: Long) = NotebookEntity(Some(id), nb.name)
  }

  implicit class NoteReqOps(n: NoteRequest) {
    def toEntity = NoteEntity(None, n.title, n.content, n.notebookId)
    def toEntity(id: Long) = NoteEntity(Some(id), n.title, n.content, n.notebookId)
  }
}
