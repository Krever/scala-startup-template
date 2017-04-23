package sst.fronted.router

import japgolly.scalajs.react.extra.router.{BaseUrl, Redirect, Resolution, Router, RouterConfigDsl, RouterCtl}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import sst.fronted.circuit.SSTCircuit
import sst.fronted.component.{Dashboard, NotebookList}

/**
  * Created by wpitula on 4/22/17.
  */
class SSTRouter {

  val reactRouter = Router(BaseUrl.until_#, routerConfig)


  // configure the router
  private def routerConfig = RouterConfigDsl[SSTRoute].buildConfig { dsl =>
    import dsl._

    //    val todoWrapper = SPACircuit.connect(_.notebooks)
    // wrap/connect components to the circuit
    (staticRoute(root, NotebooksRoute) ~> renderR(ctl => SSTCircuit.wrap(_.notebooks)(proxy => Dashboard(ctl, proxy)))
      | dynamicRouteCT("#notebook" / long.caseClass[SingleNotebookRoute]) ~> renderR(ctl => SSTCircuit.wrap(_.notebooks)(proxy => Dashboard(ctl, proxy)))
      ).notFound(redirectToPage(NotebooksRoute)(Redirect.Replace))
  }.renderWith(layout)

  private val notebooksWrapper = SSTCircuit.connect(_.notebooks.toOption)

  private def layout(c: RouterCtl[SSTRoute], r: Resolution[SSTRoute]) = {
    <.div(^.`class` := "ui container",
      //Title(),
      <.div(^.`class` := "ui grid",
        <.div(^.`class` := "three wide column",
          notebooksWrapper(proxy => NotebookList(c, r.page, proxy))
        ),
        <.div(^.`class` := "thirteen wide column",
          r.render()
        )
      )
    )
  }

}
