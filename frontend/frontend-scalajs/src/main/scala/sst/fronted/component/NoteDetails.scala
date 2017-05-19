package sst.fronted.component

import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ScalaComponent}
import slogging.LazyLogging
import sst.fronted.circuit.{DeleteNote, RefreshNotebooks, RefreshNotes, UpdateNote}
import sst.fronted.router.{NoteDetailsRoute, NotebookDetailsRoute, NotebooksRoute, SSTRoute}
import sst.shared.{Note, Notebook}


object NoteDetails extends LazyLogging {

  private val component = ScalaComponent.builder[Props]("NotebookDetails")
    .initialState_P(p => State(p.model.value.note.getOrElse(Note(-1, "", "", p.currentLoc.notebookId))))
    .renderBackend[Backend]
    .componentDidMount(scope =>
      for {
        _ <- Callback.when(!scope.props.model.value.notebook.isReady)(scope.props.model.dispatchCB(RefreshNotebooks))
        _ <- Callback.when(!scope.props.model.value.note.isReady)(scope.props.model.dispatchCB(RefreshNotes(scope.props.currentLoc.notebookId)))
      } yield ()
    ).build

  def apply(ctl: RouterCtl[SSTRoute], currentLoc: NoteDetailsRoute, proxy: ModelProxy[Model]): Unmounted[Props, State, Backend] = component(Props(ctl, currentLoc, proxy))

  private def isReady(model: Model): Boolean = model.note.isReady && model.notebook.isReady

  case class Props(ctl: RouterCtl[SSTRoute], currentLoc: NoteDetailsRoute, model: ModelProxy[Model])

  case class State(note: Note)

  case class Model(notebook: Pot[Notebook], note: Pot[Note])

  class Backend(t: BackendScope[Props, State]) {

    def render(p: Props, s: State): VdomElement = {
      logger.debug(s"Rendering ${p.model}")
      if (isReady(p.model.value)) {
        val notebook = p.model.value.notebook.get
        val note = s.note
        <.div(
          <.div(^.`class` := "ui basic clearing segment",
            <.div(^.`class` := "ui breadcrumb",
              p.ctl.link(NotebooksRoute)(^.`class` := "section", "Notebooks"),
              <.div(^.`class` := "divider", "/"),
              p.ctl.link(NotebookDetailsRoute(p.currentLoc.notebookId))(^.`class` := "section", notebook.name),
              <.div(^.`class` := "divider", "/"),
              <.div(^.`class` := "active section", p.model.value.note.get.title)
            ),
            <.button(
              ^.`class` := "ui right floated icon button",
              ^.onClick --> p.ctl.set(NotebookDetailsRoute(notebook.id)).flatMap(_ => p.model.dispatchCB(DeleteNote(note))),
              <.i(^.`class` := "trash icon")
            )
          ),
          <.div(^.`class` := "ui form",
            <.div(^.`class` := "field",
              <.label("Title"),
              <.input(
                ^.onChange ==> updateTitle,
                ^.value := note.title
              )
            ),
            <.div(^.`class` := "field",
              <.label("Content"),
              <.textarea(
                ^.onChange ==> updateContent,
                note.content)
            ),
            <.button(
              ^.`class` := "ui button",
              ^.onClick --> p.model.dispatchCB(UpdateNote(note)),
              "Save")
          )
        )
      } else {
        <.div
      }
    }

    private def updateTitle(e: react.ReactEventFromInput) = {
      val title = e.target.value
      t.modState(s => s.copy(note = s.note.copy(title = title)))
    }

    private def updateContent(e: react.ReactEventFromTextArea) = {
      val content = e.target.value
      t.modState(s => s.copy(note = s.note.copy(content = content)))
    }
  }

}