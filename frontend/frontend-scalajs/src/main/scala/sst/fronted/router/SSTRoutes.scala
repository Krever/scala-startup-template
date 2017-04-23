package sst.fronted.router


sealed trait SSTRoute

case object  NotebooksRoute extends SSTRoute

case class SingleNotebookRoute(id : Long) extends SSTRoute