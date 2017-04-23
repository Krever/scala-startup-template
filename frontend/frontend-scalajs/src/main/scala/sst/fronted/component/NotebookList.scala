package sst.fronted.component

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div
import sst.fronted.circuit.{CreateOrUpdateNotebook, Notebooks, RefreshNotebooks}
import sst.fronted.router.{SSTRoute, SingleNotebookRoute}
import sst.shared.Notebook

object NotebookList {

  private val component = ScalaComponent.builder[Props]("MainMenu")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(ctl: RouterCtl[SSTRoute], currentLoc: SSTRoute, proxy: ModelProxy[Option[Notebooks]]): Unmounted[Props, Unit, Backend] =
    component(Props(ctl, currentLoc, proxy))

  case class Props(router: RouterCtl[SSTRoute], currentLoc: SSTRoute, proxy: ModelProxy[Option[Notebooks]])

  class Backend($: BackendScope[Props, Unit]) {
    def mounted(props: Props): Callback = Callback.when(props.proxy.value.isEmpty)(props.proxy.dispatchCB(RefreshNotebooks))

    def render(props: Props): TagOf[Div] = {
      val entries = for (notebook <- props.proxy.value.map(_.items).getOrElse(Seq())) yield {
        val route = SingleNotebookRoute(notebook.id)
        <.a(
          ^.`class` := "item",
          ^.href := props.router.urlFor(SingleNotebookRoute(notebook.id)).value,
          (^.className := "active").when(props.currentLoc == route),
          notebook.name
        )
      }
      <.div(^.`class` := "ui vertical fluid tabular menu",
        <.a(
          ^.`class` := "item",
          ^.onClick --> props.proxy.dispatchCB(CreateOrUpdateNotebook(Notebook(-1, "New Notebook"))),
          "Create new notebook"
        ),
        entries.toTagMod
      )
    }

  }

}