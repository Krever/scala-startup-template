package sst.backend

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import slick.jdbc.SQLiteProfile
import slogging.{LazyLogging, LoggerConfig}
import sst.backend.data._
import sst.backend.data.tables.{NotebooksRepository, NotesRepository}
import sst.backend.routes.{ApiRoutes, FrontedRoutes}

import scala.util.{Failure, Success}

object Main extends LazyLogging {

  def main(args: Array[String]): Unit = {

    LoggerConfig.factory = slogging.PrintLoggerFactory()
    LoggerConfig.level = slogging.LogLevel.DEBUG

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    val config = ConfigFactory.load

    val slickProfile = SQLiteProfile
    val notebooksRepository = new NotebooksRepository(slickProfile)
    val notesRepository = new NotesRepository(slickProfile, notebooksRepository)
    val dbConfig = new DBConfig(config)
    val dbExecutor = new DBExecutor(slickProfile, dbConfig)
    val dbMigrator = new DBMigrator(dbConfig)
    val apiRoutes = new ApiRoutes(notesRepository, notebooksRepository, dbExecutor)

    import akka.http.scaladsl.server.Directives._

    val address = "0.0.0.0"
    val port = config.getInt("sst.backend.port")

    val route = Directives.logRequestResult(("log", Logging.WarningLevel)){
      apiRoutes.routes ~ FrontedRoutes.routes
    }

    dbMigrator.migrate() match {
      case Success(num) => logger.debug(s"Sucessfully applied $num migrations")
      case Failure(ex) =>
        logger.debug(s"Application of migrations failed", ex)
        system.terminate()
        System.exit(1)
    }

    val _ = Http().bindAndHandle(route,
                                 address,
                                 port)

    println(s"Server online at http://$address:$port/")
    println("Press RETURN to stop...")

  }

}
