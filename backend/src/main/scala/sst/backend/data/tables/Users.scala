package sst.backend.data.tables

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile

case class UserEntity(id: Option[Long], username: String, passwordHash: String, salt: String)
    extends Entity[UserEntity, Long] {
  def withId(id: Long): UserEntity = this.copy(id = Some(id))
}

class UsersRepository(override val driver: JdbcProfile) extends Repository[UserEntity, Long](driver) {

  import driver.api._

  type TableType = Users
  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = TableQuery[Users]

  class Users(tag: slick.lifted.Tag) extends Table[UserEntity](tag, "USERS") with Keyed[Long] {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def username = column[String]("USERNAME")

    def passwordHash = column[String]("PASSWORD_HASH")

    def salt = column[String]("SALT")

    def * = (id.?, username, passwordHash, salt) <> (UserEntity.tupled, UserEntity.unapply)

    def usernameUniq = index("USERNAME_UNIQ", username, true)

  }

  def findByUsername(username: String): DBIO[Option[UserEntity]] =
    tableQuery.filter(_.username === username).result.headOption

}
