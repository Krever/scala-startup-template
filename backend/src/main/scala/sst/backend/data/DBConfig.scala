package sst.backend.data

import com.typesafe.config.Config

/**
  * Created by wpitula on 6/11/17.
  */
class DBConfig(config: Config) {

  val jdbcUrl: String = config.getString("sst.backend.db.jdbc-url")

  val user: String = ""

  val password: String = ""

}
