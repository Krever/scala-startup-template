package sst.fronted

import org.scalajs.dom
import sst.fronted.router.SSTRouter

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


@JSExportTopLevel("Main")
object Main extends js.JSApp {

  @JSExport
  def main(): Unit = {
    val router = new SSTRouter
    val _ = router.reactRouter().renderIntoDOM(dom.document.getElementById("root"))
  }

}


/*

  private lazy val MainTemplate = ScalaComponent.build[String]("Main")
    .render_P(name =>
      <.div(^.`class` := "ui container",
        Title(),
        <.div(^.`class` := "ui grid",
          <.div(^.`class` := "three wide column",
            LeftMenu()
          ),
          <.div(^.`class` := "thirteen wide column"

          )
        )
      )
    )
    .build

  private lazy val Title = ScalaComponent.static("Title",
    <.h1(
      ^.`class` := "ui huge header",
      ^.style := js.Dictionary("text-align" -> "center"),
      "Notebooks")

  )

  private lazy val LeftMenu = {
    val MenuEntry = ScalaComponent.build[String]("MenuEntry")
      .render_P(text =>
        <.a(^.`class` := "item",
          text
        )).build

    ScalaComponent.static("LeftMenu",
      <.div(^.`class` := "ui vertical fluid tabular menu",
        MenuEntry("Entry 1"),
        MenuEntry("Entry 2")
      )
    )
  }
 */