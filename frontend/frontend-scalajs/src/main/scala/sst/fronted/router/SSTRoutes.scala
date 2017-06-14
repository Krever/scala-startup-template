package sst.fronted.router

sealed trait SSTRoute

case object NotebooksRoute extends SSTRoute

case class NotebookDetailsRoute(notebookId: Long) extends SSTRoute

case class NoteDetailsRoute(notebookId: Long, noteId: Long) extends SSTRoute

case class LoginRoute(redirectTo: Option[SSTRoute]) extends SSTRoute
