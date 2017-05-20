package sst.fronted.router

import diode.data.Pot
import japgolly.scalajs.react.extra.router.{
  BaseUrl,
  Redirect,
  Resolution,
  Router,
  RouterConfigDsl,
  RouterCtl
}
import japgolly.scalajs.react.vdom.html_<^.{<, _}
import sst.fronted.circuit.SSTCircuit
import sst.fronted.component.{NoteDetails, NotebookDetails, NotebookList}

/**
  * Created by wpitula on 4/22/17.
  */
class SSTRouter {

  val reactRouter = Router(BaseUrl.until_#, routerConfig)

  private val notebooksWrapper = SSTCircuit.connect(_.notebooks)

  // configure the router
  private def routerConfig =
    RouterConfigDsl[SSTRoute]
      .buildConfig { implicit dsl =>
        import dsl._
        (notebooksRoute
          | notebookDetailsRoute
          | notesRoute).notFound(
          redirectToPage(NotebooksRoute)(Redirect.Replace))
      }
      .renderWith(layout)

  private def layout(c: RouterCtl[SSTRoute], r: Resolution[SSTRoute]) = {
    <.div(
      ^.`class` := "ui container",
      <.div(
        ^.`class` := "ui secondary pointing menu",
        <.a(
          ^.`class` := "item",
          ^.href := c.urlFor(NotebooksRoute).value,
          (^.className := "active").when(
            c.pathFor(r.page)
              .value
              .startsWith(c.pathFor(NotebooksRoute).value)),
          "Notebooks"
        )
      ),
      <.div(^.`class` := "ui segment", r.render())
    )
  }

  private def notebooksRoute(
      implicit dsl: RouterConfigDsl[SSTRoute]): dsl.Rule = {
    import dsl._
    staticRoute(root, NotebooksRoute) ~> renderR(ctl =>
      notebooksWrapper(proxy => NotebookList(ctl, NotebooksRoute, proxy)))
  }

  private def notebookDetailsRoute(
      implicit dsl: RouterConfigDsl[SSTRoute]): dsl.Rule = {
    import dsl._
    dynamicRouteCT("#notebooks" / long.caseClass[NotebookDetailsRoute]) ~>
      dynRenderR {
        case (route @ NotebookDetailsRoute(id), router) =>
          val wrapper = SSTCircuit.connect(m =>
            NotebookDetails.Model(m.notebooks.flatMap(x =>
                                    Pot.fromOption(x.items.find(_.id == id))),
                                  m.notes))
          wrapper(proxy => NotebookDetails(router, route, proxy))
      }
  }

  private def notesRoute(implicit dsl: RouterConfigDsl[SSTRoute]): dsl.Rule = {
    import dsl._
    dynamicRouteCT(
      "#notebooks" / (long / "notes" / long).caseClass[NoteDetailsRoute]) ~>
      dynRenderR {
        case (route @ NoteDetailsRoute(notebookId, noteId), router) =>
          val wrapper = SSTCircuit.connect(
            m =>
              NoteDetails.Model(
                m.notebooks.flatMap(x =>
                  Pot.fromOption(x.items.find(_.id == notebookId))),
                m.notes.flatMap(x =>
                  Pot.fromOption(x.items.find(_.id == noteId)))))
          wrapper(proxy => NoteDetails(router, route, proxy))
      }
  }

}
