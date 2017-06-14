package sst.shared

@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait ApiEndpoints extends ApiBase with NotebookApi with NotesAPI {

  import io.circe.generic.auto._

  def apiBasePath: Path[Unit] = path / "api"


  val login: Endpoint[Credentials, RawSession[Session]] =
    anEndpoint
      .withMethod(Post)
      .withUrl(path / "login")
      .withJsonRequest[Credentials]
      .withResponse(sessionSet(emptyResponse))
      .build[Credentials]


}

case class Credentials(login: String, password: String)

case class Session(username: String)
