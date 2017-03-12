package sst.backend

import java.util.UUID

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import endpoints.akkahttp
import sst.shared.{ApiEndpoints, Note, Notebook}


object ApiRoutes extends ApiEndpoints with akkahttp.routing.Endpoints with akkahttp.routing.CirceEntities {

  private var data: Map[String, (Notebook, Map[String, Note])] = Seq(
    (Notebook("1", "Notebook 1"), Seq(Note("1", "Note 1", "Note 1 content")))
  ).map(x => (x._1.id, x)).toMap.mapValues(x => (x._1, x._2.map(n => (n.id, n)).toMap))

  def routes: Route =
    getNotebooks.implementedBy{_ =>
      data.values.map(_._1)
    } ~
      createNotebook.implementedBy { req =>
        val id = randomId()
        val notebook = Notebook(id, req.name)
        data += ((id, (notebook, Map[String, Note]())))
        notebook
      } ~
      deleteNotebook.implementedBy { id =>
        data -= id
      } ~
      updateNotebook.implementedBy { case (id, req) =>
        data -= id
        val notebook = Notebook(id, req.name)
        data += ((id, (notebook, Map[String, Note]())))
      } ~
      getNotesFromNotebook.implementedBy { id =>
        data(id)._2.values
      }


  private def randomId(): String = UUID.randomUUID().toString

  override def apiBasePath: Path[Unit] = path / "api"
}
