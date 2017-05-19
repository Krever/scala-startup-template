package sst.fronted.component

import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div
import slogging.LazyLogging
import sst.fronted.circuit.{Notebooks, RefreshNotebooks, UpdateNotebook}
import sst.fronted.router.{NotebookDetailsRoute, SSTRoute}
import sst.shared.Notebook

object NotebookList extends LazyLogging {

  private val component = ScalaComponent.builder[Props]("NotebookList")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(ctl: RouterCtl[SSTRoute], currentLoc: SSTRoute, proxy: ModelProxy[Pot[Notebooks]]): Unmounted[Props, Unit, Backend] =
    component(Props(ctl, currentLoc, proxy))

  case class Props(router: RouterCtl[SSTRoute], currentLoc: SSTRoute, proxy: ModelProxy[Pot[Notebooks]])

  class Backend() {
    def mounted(props: Props): Callback = Callback.when(props.proxy.value.isEmpty)(props.proxy.dispatchCB(RefreshNotebooks))

    def render(props: Props): TagOf[Div] = {
      <.div(
        <.div(^.`class` := "ui basic clearing segment",
          <.div(^.`class` := "ui breadcrumb",
            <.div(^.`class` := "active section", "Notebooks")
          )
        ),
        <.div(^.`class` := "ui relaxed divided list",
          <.a(
            ^.`class` := "item",
            ^.onClick --> props.proxy.dispatchCB(UpdateNotebook(Notebook(-1, "New Notebook"))),
            <.i(^.`class` := "plus icon"),
            "Create new notebook"
          ),
          (for (notebook <- props.proxy.value.map(_.items).getOrElse(Seq())) yield {
            props.router.link(NotebookDetailsRoute(notebook.id))(
              ^.`class` := "item",
              notebook.name
            )
          }).toTagMod
        )
      )
    }

  }

}