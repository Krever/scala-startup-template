package sst.backend.data.tables

import com.byteslounge.slickrepo.meta.{Entity, Keyed}
import com.byteslounge.slickrepo.repository.Repository
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile

case class NoteEntity(id: Option[Long], title: String, content: String, notebookId: Long)
    extends Entity[NoteEntity, Long] {
  def withId(id: Long): NoteEntity = this.copy(id = Some(id))
}

class NotesRepository(override val driver: JdbcProfile, val notebooksRepository: NotebooksRepository)
    extends Repository[NoteEntity, Long](driver) {

  import driver.api._

  type TableType = Notes
  val pkType = implicitly[BaseTypedType[Long]]
  val tableQuery = TableQuery[Notes]

  class Notes(tag: slick.lifted.Tag) extends Table[NoteEntity](tag, "NOTES") with Keyed[Long] {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def title = column[String]("TITLE")

    def content = column[String]("CONTENT")

    def notebookId = column[Long]("NOTEBOOK_ID")

    def notebook = foreignKey("NOTEBOOK_FK", notebookId, notebooksRepository.tableQuery)(_.id)

    def * = (id.?, title, content, notebookId) <> (NoteEntity.tupled, NoteEntity.unapply)

  }

  def findAllForNotebook(id: Long): DBIO[Seq[NoteEntity]] = tableQuery.filter(_.notebookId === id).result

}
