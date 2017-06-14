package sst.fronted.circuit

import diode._
import diode.data._
import diode.react.ReactConnector
import sst.fronted.service.ApiClient

object SSTCircuit extends Circuit[RootModel] with ReactConnector[RootModel] {

  val apiClient = new ApiClient()

  override protected val actionHandler: HandlerFunction = composeHandlers(
    new NotebooksHandler(zoomRW(x => (x.notebooks, x.session))((m, v) => m.copy(notebooks = v._1, session = v._2)), apiClient),
    new NotesHandler(zoomRW(_.notes)((m, v) => m.copy(notes = v)), apiClient),
    new LoginHandler(zoomRW(_.session)((m, v) => m.copy(session = v)), apiClient)
  )

  override protected def initialModel = RootModel(Empty, Empty, None)
}
