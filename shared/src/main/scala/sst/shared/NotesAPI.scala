package sst.shared


@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait NotesAPI { self: ApiBase =>

  import io.circe.generic.auto._

  private val allNotesPath: Path[Unit] = apiBasePath / "notes"
  private val singleNotePath = allNotesPath / segment[Long]

  val createNote: Endpoint[NoteRequest, Unit] =
    endpoint(
      post[Unit, NoteRequest, Unit, NoteRequest](
        allNotesPath,
        jsonRequest[NoteRequest]),
      emptyResponse
    )

  val deleteNote: Endpoint[Long, Unit] =
    endpoint(
      request[Long, Unit, Unit, Long](
        Delete,
        singleNotePath),
      emptyResponse
    )

  val updateNote: Endpoint[(Long, NoteRequest), Unit] = endpoint(
    request[Long, NoteRequest, Unit, (Long, NoteRequest)](
      Put,
      singleNotePath,
      jsonRequest[NoteRequest]),
    emptyResponse
  )

}

case class Note(id: Long, title: String, content: String, notebookId: Long)
case class NoteRequest(title: String, content: String, notebookId: Long)