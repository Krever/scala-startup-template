package sst.fronted

import org.scalajs.dom
import sst.fronted.router.SSTRouter

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


@JSExportTopLevel("Main")
object Main extends js.JSApp {

  @JSExport
  def main(): Unit = {
    slogging.LoggerConfig.factory = slogging.ConsoleLoggerFactory()
    slogging.LoggerConfig.level = slogging.LogLevel.DEBUG

    val router = new SSTRouter
    val rootElem = dom.document.getElementById("root")
    val _ = router.reactRouter().renderIntoDOM(rootElem)
  }

}