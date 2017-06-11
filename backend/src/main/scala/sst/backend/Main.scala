package sst.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import slick.jdbc.SQLiteProfile
import slogging.{LazyLogging, LoggerConfig}
import sst.backend.data._
import sst.backend.data.tables.{NotebooksRepository, NotesRepository}
import sst.backend.routes.{ApiRoutes, FrontedRoutes}
import sst.backend.util.AccessLogHelper

import scala.util.{Failure, Success}

object Main extends LazyLogging {

  def main(args: Array[String]): Unit = {
    LoggerConfig.factory = slogging.SLF4JLoggerFactory()
    LoggerConfig.level = slogging.LogLevel.TRACE

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

    val address = config.getString("sst.backend.http.interface")
    val port = config.getInt("sst.backend.http.port")

    val route = AccessLogHelper.logAccess{
      apiRoutes.routes ~ FrontedRoutes.routes
    }

    dbMigrator.migrate() match {
      case Success(num) => logger.debug(s"Successfully applied $num migrations")
      case Failure(ex) =>
        logger.error(s"Application of migrations failed", ex)
        system.terminate()
        System.exit(1)
    }

    val _ = Http().bindAndHandle(route,
                                 address,
                                 port)

    logger.info(s"Server online at http://$address:$port/")

  }

}
