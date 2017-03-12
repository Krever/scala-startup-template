package sst.shared

import endpoints.algebra.{CirceEntities, Endpoints}


@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait NotesAPI extends Endpoints with CirceEntities {

  import io.circe.generic.auto._

  private val allNotesPath: Path[Unit] = path / "notes"
  private val singleNotePath = allNotesPath / segment[String]

  val createNote: Endpoint[NoteRequest, Unit] =
    endpoint(
      post[Unit, NoteRequest, Unit, NoteRequest](
        allNotesPath,
        jsonRequest[NoteRequest]),
      emptyResponse
    )

  val deleteNote: Endpoint[String, Unit] =
    endpoint(
      request[String, Unit, Unit, String](
        Delete,
        singleNotePath),
      emptyResponse
    )

  val updateNote: Endpoint[(String, NoteRequest), Unit] = endpoint(
    request[String, NoteRequest, Unit, (String, NoteRequest)](
      Put,
      singleNotePath,
      jsonRequest[NoteRequest]),
    emptyResponse
  )

}

case class Note(id: String, title: String, content: String)
case class NoteRequest(title: String, content: String)