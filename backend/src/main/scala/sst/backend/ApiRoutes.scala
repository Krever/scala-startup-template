package sst.backend

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import endpoints.akkahttp
import sst.shared.{ApiEndpoints, Note, Notebook}

import scala.util.Random


object ApiRoutes extends ApiEndpoints with akkahttp.routing.Endpoints with akkahttp.routing.CirceEntities {

  private var data: Map[Long, (Notebook, Map[Long, Note])] = (1L to 6L).map( i =>
    (Notebook(i, s"Notebook $i"), Seq(Note(i, s"Note $i", s"Note $i content", i)))
  ).map(x => (x._1.id, x)).toMap.mapValues(x => (x._1, x._2.map(n => (n.id, n)).toMap))

  def routes: Route =
    getNotebooks.implementedBy{_ =>
      data.values.map(_._1).toList.sortBy(_.name)
    } ~
      createNotebook.implementedBy { req =>
        val id = randomId()
        val notebook = Notebook(id, req.name)
        data += ((id, (notebook, Map[Long, Note]())))
        notebook
      } ~
      deleteNotebook.implementedBy { id =>
        data -= id
      } ~
      updateNotebook.implementedBy { case (id, req) =>
        data -= id
        val notebook = Notebook(id, req.name)
        data += ((id, (notebook, Map[Long, Note]())))
      } ~
      getNotesFromNotebook.implementedBy { id =>
        data(id)._2.values
      }


  private def randomId(): Long = Random.nextLong()

  override def apiBasePath: Path[Unit] = path / "api"
}
