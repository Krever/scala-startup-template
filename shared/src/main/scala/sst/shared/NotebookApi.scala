package sst.shared

import endpoints.algebra.CirceEntities

@SuppressWarnings(Array("AsInstanceOf", "org.wartremover.warts.AsInstanceOf"))
trait NotebookApi extends ApiBase with CirceEntities{

  import io.circe.generic.auto._

  private val allNotebooksPath: Path[Unit] = path / "notebooks"
  private val singleNotebookPath = allNotebooksPath / segment[Long]


  val getNotebooks: Endpoint[Unit, Iterable[Notebook]] =
    endpoint(
      get(allNotebooksPath),
      jsonResponse[Iterable[Notebook]]
    )

  val createNotebook: Endpoint[NotebookRequest, Notebook] =
    endpoint(
      post[Unit, NotebookRequest, Unit, NotebookRequest](
        allNotebooksPath,
        jsonRequest[NotebookRequest]),
      jsonResponse[Notebook]
    )

  val deleteNotebook: Endpoint[Long, Unit] =
    endpoint(
      request[Long, Unit, Unit, Long](
        Delete,
        singleNotebookPath),
      emptyResponse
    )

  val updateNotebook: Endpoint[(Long, NotebookRequest), Unit] = endpoint(
    request[Long, NotebookRequest, Unit, (Long, NotebookRequest)](
      Put,
      singleNotebookPath,
      jsonRequest[NotebookRequest]),
    emptyResponse
  )

    val getNotesFromNotebook: Endpoint[Long, Iterable[Note]] =
      endpoint(
        get(singleNotebookPath / "notes"),
        jsonResponse[Iterable[Note]]
      )

}


case class Notebook(id: Long, name: String)

case class NotebookRequest(name: String)