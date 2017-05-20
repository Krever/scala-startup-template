package sst.fronted.component

import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{Callback, ScalaComponent}
import org.scalajs.jquery.jQuery
import sst.fronted.circuit.{DeleteNotebook, Notes, RefreshNotebooks}
import sst.fronted.router.{NotebooksRoute, SSTRoute}
import sst.shared.Notebook

import scala.scalajs.js

object NotebookDetails {

  private val component = ScalaComponent
    .builder[Props]("NotebookDetails")
    .renderP { (_, props) =>
      if (props.model.value.notebook.isReady) {
        val model = props.model.value.notebook.get
        <.div(
          <.div(
            ^.`class` := "ui basic clearing segment",
            <.div(
              ^.`class` := "ui breadcrumb",
              props.ctl.link(NotebooksRoute)(^.`class` := "section",
                                             "Notebooks"),
              <.div(^.`class` := "divider", "/"),
              <.div(^.`class` := "active section", model.name)
            ),
            <.button(
              ^.`class` := "ui right floated icon button",
              ^.onClick --> props.model
                .dispatchCB(DeleteNotebook(model))
                .flatMap(_ => props.ctl.set(NotebooksRoute)),
              <.i(^.`class` := "trash icon")
            ),
            <.button(
              ^.`class` := "ui right floated icon button",
              ^.onClick --> Callback(
                jQuery("#rename-modal")
                  .asInstanceOf[js.Dynamic]
                  .modal("show")),
              <.i(^.`class` := "edit icon")
            )
          ),
          NotesList(props.ctl,
                    props.currentLoc,
                    model.id,
                    props.model.zoom(_.notes)),
          NotebookRenameModal(props.model.zoom(_.notebook.get))
        )
      } else {
        <.div
      }
    }
    .componentDidMount(scope =>
      Callback.when(!scope.props.model.value.notebook.isReady)(
        scope.props.model.dispatchCB(RefreshNotebooks)))
    .build

  def apply(ctl: RouterCtl[SSTRoute],
            currentLoc: SSTRoute,
            proxy: ModelProxy[Model]): Unmounted[Props, Unit, Unit] =
    component(Props(ctl, currentLoc, proxy))

  case class Props(ctl: RouterCtl[SSTRoute],
                   currentLoc: SSTRoute,
                   model: ModelProxy[Model])

  case class Model(notebook: Pot[Notebook], notes: Pot[Notes])

}
