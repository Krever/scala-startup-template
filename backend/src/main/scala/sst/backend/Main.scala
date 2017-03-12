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

    val _ = Http().bindAndHandle(ApiRoutes.routes ~ FrontedRoutes.routes, "0.0.0.0", 8080)

    println("Server online at http://localhost:8080/\nPress RETURN to stop...")
//    StdIn.readLine()
//    bindingFuture
//      .flatMap(_.unbind())
//      .onComplete(_ => system.terminate())
  }

}
