package sst.fronted.circuit

import diode.Action
import sst.shared.{Note, NoteRequest, Notebook, NotebookRequest}

//Notebooks
case object RefreshNotebooks extends Action

case class UpdateAllNotebooks(notebooks: Seq[Notebook]) extends Action

case class CreateNotebook(notebookRequest: NotebookRequest) extends Action

case class UpdateNotebook(notebook: Notebook) extends Action

case class DeleteNotebook(notebook: Notebook) extends Action

//Notes
case class RefreshNotes(notebookId: Long) extends Action

case class UpdateAllNotes(notebookId: Long, notes: Seq[Note]) extends Action

case class CreateNote(noteRequest: NoteRequest) extends Action

case class UpdateNote(note: Note) extends Action

case class DeleteNote(note: Note) extends Action


// Authentication test
case class Login(username: String, password: String) extends Action

case class SetSession(session: String) extends Action

case class Error(msg: String) extends Action

