package sst.fronted

import org.scalajs.dom.document

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    val _ = test()
//    appendPar(document.body, HelloWorld("from Scalajs").toString)
  }

  def test() = {
    import japgolly.scalajs.react._
    import japgolly.scalajs.react.vdom.html_<^._
    val Hello =
      ScalaComponent.build[String]("Hello")
        .render_P(name => <.div("Hello there ", name))
        .build

    // Usage:
    Hello("Worldd").renderIntoDOM(document.body)
  }
}
