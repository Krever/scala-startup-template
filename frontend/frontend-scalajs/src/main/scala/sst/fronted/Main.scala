package sst.fronted

import org.scalajs.dom
import org.scalajs.dom.document
import sst.shared.HelloWorld

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {
    appendPar(document.body, HelloWorld("from Scalajs").toString)
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
    ()
  }
}
