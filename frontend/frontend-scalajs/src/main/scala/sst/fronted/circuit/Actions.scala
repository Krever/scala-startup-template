package sst.fronted.circuit

import diode.Action
import sst.shared.Notebook

case object RefreshNotebooks extends Action

case class UpdateAllNotebooks(notebooks: Seq[Notebook]) extends Action

case class CreateOrUpdateNotebook(notebook: Notebook) extends Action

//case class RefreshNotes(notebookId: Int) extends Action
//
//case class UpdateAllNotebooks(notebooks: Seq[Notebook]) extends Action
//
//case class UpdateAllNotes(notebookId: Int, notes: Seq[Note]) extends Action
//
//case class UpdateNotebook(notebook: Notebook) extends Action
//
//case class UpdateNote(note: Note) extends Action
//
//case class CreateNotebook(notebookRequest: NotebookRequest) extends Action
//
//case class CreateNote(noteRequest: NoteRequest) extends Action
//
case class DeleteNotebook(notebook: Notebook) extends Action
//
//case class DeteleteNote(note: Note) extends Action