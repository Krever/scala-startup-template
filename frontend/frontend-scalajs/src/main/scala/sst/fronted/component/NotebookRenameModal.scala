package sst.fronted.component

import diode.react.ModelProxy
import japgolly.scalajs.react
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div
import slogging.LazyLogging
import sst.fronted.circuit._
import sst.shared.Notebook

object NotebookRenameModal {

  private val component = ScalaComponent.builder[Props]("Notes")
    .initialStateFromProps(p => State(p.proxy.value.name))
    .renderBackend[Backend]
    .build

  def apply(proxy: ModelProxy[Notebook]): Unmounted[Props, State, Backend] = component(Props(proxy))

  case class Props(proxy: ModelProxy[Notebook])

  case class State(notebookName: String)

  class Backend(t: BackendScope[Props, State]) extends LazyLogging {

    def updateName(event: react.ReactEventFromInput): CallbackTo[Unit] = {
      logger.debug("Updating notebook name", event)
      val title = event.target.value
      t.modState(s => s.copy(notebookName = title))
    }

    def render(props: Props, state: State): TagOf[Div] = {
      <.div(
        ^.`class` := "ui modal", ^.id := "rename-modal",
        <.div(^.`class` := "header", "Rename notebook"),
        <.div(^.`class` := "description",
          <.div(^.`class` := "segment",
            <.div(^.`class` := "ui form",
              <.div(^.`class` := "inline fields",
                <.div(^.`class` := "field",
                  <.label("Name"),
                  <.input(
                    ^.id := "name-field",
                    ^.value := state.notebookName,
                    ^.onChange ==> updateName
                  )
                )
              )
            )
          )
        ),
        <.div(^.`class` := "actions",
          <.div(^.`class` := "ui black deny button", "Cancel"),
          <.div(^.`class` := "ui positive right labeled icon button",
            "Save",
            <.i(^.`class` := "checkmark icon"),
            ^.onClick --> props.proxy.dispatchCB(UpdateNotebook(props.proxy.value.copy(name = state.notebookName)))
          )
        )
      )
    }
  }

}