package sst.shared

@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait ApiEndpoints extends ApiBase with NotebookApi with NotesAPI {

  import io.circe.generic.auto._

  def apiBasePath: Path[Unit] = path / "api"


  val login: Endpoint[Credentials, Option[RawSession[Session]]] =
    anEndpoint
      .withMethod(Post)
      .withUrl(apiBasePath / "login")
      .withJsonRequest[Credentials]
      .withResponse(authorized(sessionSet(emptyResponse)))
      .build[Credentials]

  val register: Endpoint[Credentials, Either[BadRequest, Unit]] =
    anEndpoint
      .withMethod(Post)
      .withUrl(apiBasePath / "register")
      .withJsonRequest[Credentials]
      .withResponse(validatedResponse(emptyResponse))
      .build[Credentials]


}

case class Credentials(login: String, password: String)

case class Session(username: String)
