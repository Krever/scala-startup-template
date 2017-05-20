package sst.fronted.circuit

import diode._
import diode.data._
import diode.react.ReactConnector

object SSTCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {
  override protected val actionHandler: HandlerFunction = composeHandlers(
    new NotebooksHandler(zoomRW(_.notebooks)((m, v) => m.copy(notebooks = v))),
    new NotesHandler(zoomRW(_.notes)((m, v) => m.copy(notes = v)))
  )

  override protected def initialModel = RootModel(Empty, Empty)
}
