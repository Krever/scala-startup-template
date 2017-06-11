package sst.backend.data

import slick.jdbc.JdbcProfile

import scala.concurrent.Future

class DBExecutor(val driver: JdbcProfile, config: DBConfig) {

  import driver.api._

  val db = Database.forURL(config.jdbcUrl)

  implicit class DBIOOps[T](dbio: DBIO[T]){
    def run: Future[T] = db.run(dbio)
    def runTransactionally: Future[T] = db.run(dbio.transactionally)
  }

}
