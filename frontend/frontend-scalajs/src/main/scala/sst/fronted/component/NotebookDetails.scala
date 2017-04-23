package sst.fronted.component

import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react.{Callback, ScalaComponent}
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import sst.fronted.circuit.{DeleteNotebook, RefreshNotebooks}
import sst.shared.Notebook
import scala.scalajs.js
import js.Dynamic.{ global => g }

object NotebookDetails {

  private val component = ScalaComponent.builder[Props]("NotebookDetails")
    .renderP { (_, props) =>
      g.console.log(s"Rendering ${props.notebook}")
      if(props.notebook.value.isReady) {
        val model = props.notebook.value.get
        <.div(
          <.h2(
            model.name
          ),
          <.button(^.`class` := "ui button",
            "Rename"
          ),
          <.button(
            ^.`class` := "ui negative button",
            ^.onClick --> props.notebook.dispatchCB(DeleteNotebook(model)),
            "Delete"
          )
        )
      } else {
        <.div
      }
    }
    .componentDidMount(scope => Callback.when(!scope.props.notebook.value.isReady)(scope.props.notebook.dispatchCB(RefreshNotebooks)))
    .build

  def apply(proxy: ModelProxy[Pot[Notebook]]): Unmounted[Props, Unit, Unit] = component(Props(proxy))

  case class Props(notebook: ModelProxy[Pot[Notebook]])

}