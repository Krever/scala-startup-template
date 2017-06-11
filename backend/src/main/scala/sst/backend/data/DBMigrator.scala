package sst.backend.data

import org.flywaydb.core.Flyway

import scala.util.Try

/**
  * Created by wpitula on 6/11/17.
  */
class DBMigrator(dbConfig: DBConfig) {

  val flyway = new Flyway()
  flyway.setDataSource(dbConfig.jdbcUrl, dbConfig.user, dbConfig.password)
//  flyway.setBaselineOnMigrate(true)

  def migrate(): Try[Int] = Try(flyway.migrate())

}
