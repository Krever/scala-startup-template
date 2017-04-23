package sst.fronted.router

import japgolly.scalajs.react.extra.router.{BaseUrl, Redirect, Resolution, Router, RouterConfigDsl, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import sst.fronted.circuit.SSTCircuit
import sst.fronted.component.{Dashboard, NotebookDetails, NotebookList}
import scala.scalajs.js
import js.Dynamic.{ global => g }

/**
  * Created by wpitula on 4/22/17.
  */
class SSTRouter {

  val reactRouter = Router(BaseUrl.until_#, routerConfig)
  println("Router created")

  private val notebooksWrapper = SSTCircuit.connect(_.notebooks.toOption)

  // configure the router
  private def routerConfig = RouterConfigDsl[SSTRoute].buildConfig { dsl =>
    import dsl._

    (staticRoute(root, NotebooksRoute) ~> renderR(ctl => SSTCircuit.wrap(_.notebooks)(proxy => Dashboard(ctl, proxy)))
      | dynamicRouteCT("#notebook" / long.caseClass[SingleNotebookRoute]) ~>
      dynRender {
        case SingleNotebookRoute(id) =>
          g.console.log(s"Switching to $id")
          val wrapper = SSTCircuit.connect(_.notebooks.map(_.items.find(_.id == id).get))
          wrapper(proxy => NotebookDetails(proxy))
      }).notFound(redirectToPage(NotebooksRoute)(Redirect.Replace))
  }.renderWith(layout)

  private def layout(c: RouterCtl[SSTRoute], r: Resolution[SSTRoute]) = {
    <.div(^.`class` := "ui two column grid container",
      //Title(),
      <.div(^.`class` := "three wide column",
        notebooksWrapper(proxy => NotebookList(c, r.page, proxy))
      ),
      <.div(^.`class` := "thirteen wide column",
        r.render()
      )
    )

  }

}
