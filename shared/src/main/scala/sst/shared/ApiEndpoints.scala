package sst.shared

@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait ApiEndpoints extends NotebookApi with NotesAPI {

  import io.circe.generic.auto._

  val login: Endpoint[Credentials, Unit] =
    endpoint(
      post[Unit, Credentials, Unit, Credentials](
        path / "login",
        jsonRequest[Credentials]),
      emptyResponse
    )

}

case class Credentials(login: String, password: String)
