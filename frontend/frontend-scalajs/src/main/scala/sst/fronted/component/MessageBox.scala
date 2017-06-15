package sst.fronted.component

import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._
import slogging.LazyLogging
import sst.fronted.circuit._

object MessageBox extends LazyLogging {

  private val component = ScalaComponent
    .builder[Props]("Message")
    .renderBackend[Backend]
    .build

  def apply(proxy: ModelProxy[Option[Message]]): Unmounted[Props, Unit, Backend] =
    component(Props(proxy))

  case class Props(model: ModelProxy[Option[Message]])

  class Backend() {

    def render(p: Props): VdomElement = {
      import diode.react.ReactPot._
      val getType: MessageType.MessageType => String = {
        case MessageType.Error => "negative"
        case MessageType.Success => "positive"
      }
      <.div(
        Pot
          .fromOption(p.model.value)
          .render(
            m =>
              <.div(^.`class` := s"ui ${getType(m.`type`)} message",
                    <.i(^.`class` := "close icon", ^.onClick --> p.model.dispatchCB(CloseMsg)),
                    <.div(^.`class` := "header", m.text)))
      )
    }

  }

}
