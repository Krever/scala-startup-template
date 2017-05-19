package sst.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Main {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    import akka.http.scaladsl.server.Directives._

    val address = "0.0.0.0"
    val port = 8080
    val _ = Http().bindAndHandle(ApiRoutes.routes ~ FrontedRoutes.routes, address, port)

    println(s"Server online at http://$address:$port/")
    println("Press RETURN to stop...")

  }

}
