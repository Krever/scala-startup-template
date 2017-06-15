package sst.fronted.component

import diode.react.ModelProxy
import japgolly.scalajs.react
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import slogging.LazyLogging
import sst.fronted.circuit._
import sst.fronted.router.SSTRoute

object LoginForm extends LazyLogging {

  private val component = ScalaComponent
    .builder[Props]("Login")
    .initialState(State("",""))
    .renderBackend[Backend]
    .componentDidUpdate(scope => scope.backend.updated(scope.currentProps))
    .build

  def apply(
      ctl: RouterCtl[SSTRoute],
      redirectTo: Option[SSTRoute],
      proxy: ModelProxy[Option[AuthToken]]): Unmounted[Props, State, Backend] =
    component(Props(ctl, redirectTo, proxy))

  case class Props(router: RouterCtl[SSTRoute],
                   redirectTo: Option[SSTRoute],
                   proxy: ModelProxy[Option[AuthToken]])

  case class State(username: String, password: String)

  class Backend(t: BackendScope[Props, State]) {

    def updated(props: Props): Callback = {
      logger.debug(s"Login form updated: ${props.proxy.value}, ${props.redirectTo}")
      Callback.when(props.proxy.value.isDefined && props.redirectTo.isDefined)(
        props.router.set(props.redirectTo.get))
    }

    def render(p: Props, s: State): VdomElement = {
      <.div(
        <.div(
          ^.`class` := "ui form",
          <.div(^.`class` := "field",
            <.label("Username"),
            <.input(
              ^.onChange ==> update((s,v) => s.copy(username = v)),
              ^.value := s.username
            )),
          <.div(^.`class` := "field",
            <.label("Password"),
            <.input(
              ^.onChange ==> update((s,v) => s.copy(password = v)),
              ^.value := s.password
            )),
          <.button(^.`class` := "ui button",
            ^.onClick --> p.proxy.dispatchCB(Login(s.username, s.password)),
            "Login"),
          <.button(^.`class` := "ui button",
            ^.onClick --> p.proxy.dispatchCB(Register(s.username, s.password)),
            "Register")
        )
      )
    }

    private def update(f: (State, String) => State)(e: react.ReactEventFromTextArea) = {
      val v = e.target.value
      if(e.target != null)
        t.modState(f(_, v))
      else
        Callback.empty
    }

  }

}
