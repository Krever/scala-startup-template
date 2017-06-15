package sst.fronted.component

import diode.data.Pot
import diode.react.{ModelProxy, ReactConnectProxy}
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.html_<^._
import sst.fronted.circuit.Notebooks
import sst.fronted.router.SSTRoute

object Dashboard {

  private val component = ScalaComponent
    .builder[Props]("Dashboard")
    .initialStateFromProps(props => State(props.proxy.connect(m => m)))
    .renderPS { (_, props, state) =>
      <.div(
        <.h2("Dashboard")
      )
    }
    .build

  def apply(router: RouterCtl[SSTRoute], proxy: ModelProxy[Pot[Notebooks]]): Unmounted[Props, State, Unit] =
    component(Props(router, proxy))

  case class Props(router: RouterCtl[SSTRoute], proxy: ModelProxy[Pot[Notebooks]])

  case class State(notebooksWrapper: ReactConnectProxy[Pot[Notebooks]])

}
