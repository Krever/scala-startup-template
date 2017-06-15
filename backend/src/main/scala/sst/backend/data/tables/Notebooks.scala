package sst.backend.data.tables

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile

case class NotebookEntity(id: Option[Long], name: String) extends Entity[NotebookEntity, Long] {
  def withId(id: Long): NotebookEntity = this.copy(id = Some(id))
}

class NotebooksRepository(override val driver: JdbcProfile) extends Repository[NotebookEntity, Long](driver) {

  import driver.api._

  type TableType = Notebooks
  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = TableQuery[Notebooks]

  class Notebooks(tag: slick.lifted.Tag) extends Table[NotebookEntity](tag, "NOTEBOOKS") with Keyed[Long] {

    def * = (id.?, name) <> (NotebookEntity.tupled, NotebookEntity.unapply)

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")
  }

}
