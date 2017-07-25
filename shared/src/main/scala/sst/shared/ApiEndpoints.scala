package sst.shared

@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait ApiEndpoints extends ApiBase with NotebookApi with NotesAPI {

  import io.circe.generic.auto._

  def apiBasePath: Path[Unit] = path / "api"


  val login: Endpoint[Credentials, Option[RawSession[Session]]] =
    endpoint(
      post[Unit, Credentials, Unit, Credentials](apiBasePath / "login", jsonRequest[Credentials]),
      authorized(sessionSet(emptyResponse))
    )

  val register: Endpoint[Credentials, Either[BadRequest, Unit]] =
    endpoint(
      post[Unit, Credentials, Unit, Credentials](apiBasePath / "register", jsonRequest[Credentials]),
      validatedResponse(emptyResponse)
    )

}

case class Credentials(login: String, password: String)

case class Session(username: String)
