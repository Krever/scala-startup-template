package sst.fronted.component

import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div
import sst.fronted.circuit._
import sst.fronted.router.{NoteDetailsRoute, SSTRoute}
import sst.shared.NoteRequest

object NotesList {

  private val component = ScalaComponent
    .builder[Props]("Notes")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(ctl: RouterCtl[SSTRoute],
            currentLoc: SSTRoute,
            notebookId: Long,
            proxy: ModelProxy[Pot[Notes]]): Unmounted[Props, Unit, Backend] =
    component(Props(ctl, currentLoc, notebookId, proxy))

  case class Props(router: RouterCtl[SSTRoute], currentLoc: SSTRoute, notebookId: Long, proxy: ModelProxy[Pot[Notes]])

  class Backend() {
    def mounted(props: Props): Callback =
      Callback.when(props.proxy.value.isEmpty)(props.proxy.dispatchCB(RefreshNotes(props.notebookId)))

    def render(props: Props): TagOf[Div] = {
      <.div(
        ^.`class` := "ui relaxed divided list",
        <.a(
          ^.`class` := "item",
          ^.onClick --> props.proxy.dispatchCB(CreateNote(NoteRequest("New Note", "", props.notebookId))),
          <.i(^.`class` := "plus icon"),
          "Create new note"
        ),
        (for (note <- props.proxy.value.map(_.items).getOrElse(Seq())) yield {
          props.router.link(NoteDetailsRoute(props.notebookId, note.id))(
            ^.`class` := "item",
            note.title
          )
        }).toTagMod
      )
    }
  }

}
