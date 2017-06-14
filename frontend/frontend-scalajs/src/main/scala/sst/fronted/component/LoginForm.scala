package sst.fronted.component

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.TagOf
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html.Div
import slogging.LazyLogging
import sst.fronted.circuit._
import sst.fronted.router.SSTRoute

object LoginForm extends LazyLogging {

  private val component = ScalaComponent
    .builder[Props]("Login")
    .renderBackend[Backend]
    .componentDidUpdate(scope => scope.backend.updated(scope.currentProps))
    .build

  def apply(
      ctl: RouterCtl[SSTRoute],
      redirectTo: Option[SSTRoute],
      proxy: ModelProxy[Option[AuthToken]]): Unmounted[Props, Unit, Backend] =
    component(Props(ctl, redirectTo, proxy))

  case class Props(router: RouterCtl[SSTRoute],
                   redirectTo: Option[SSTRoute],
                   proxy: ModelProxy[Option[AuthToken]])

  class Backend() {

    def updated(props: Props): Callback = {
      logger.debug(s"Login form updated: ${props.proxy.value}, ${props.redirectTo}")
      Callback.when(props.proxy.value.isDefined && props.redirectTo.isDefined)(
        props.router.set(props.redirectTo.get))
    }

    def render(): TagOf[Div] = {
      <.div(
        <.div(^.`class` := "ui basic clearing segment",
              <.div(^.`class` := "ui breadcrumb",
                    <.div(^.`class` := "active section", "Login"))),
        <.div(
          ^.`class` := "ui relaxed divided list",
          <.a(
            ^.`class` := "item",
            ^.onClick --> Callback(SSTCircuit.dispatch(Login("user2", "pass2"))),
            "Login"
          )
        )
      )
    }

  }

}
