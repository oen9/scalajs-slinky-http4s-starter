package example.modules

import com.lambdaminute.slinkywrappers.reactrouter.RouteProps
import example.bridges.PathToRegexp
import example.services.ReactDiode
import example.services.AppCircuit
import scalajs.js
import slinky.core.facade.ReactElement
import slinky.core.annotations.react
import slinky.core.StatelessComponent
import com.lambdaminute.slinkywrappers.reactrouter.Route
import example.modules.MainRouter.Loc
import example.bridges.reactrouter.Switch

@react class MainRouter extends StatelessComponent {
  type Props = RouteProps

  override def componentDidMount(): Unit = {
    super.componentDidMount()
    println("router mounted")
  }

  def render(): ReactElement = {
    val routerSwitch = Switch(
      Route(exact = true, path = Loc.home, render = _ => Home()),
      Route(exact = true, path = Loc.about, render = _ => About()),
      Route(exact = true, path = Loc.page3, render = _ => DynamicPage())
    )
    ReactDiode.diodeContext.Provider(AppCircuit)(
      Layout(routerSwitch)
    )
  }
}

object MainRouter {
  case class MenuItem(idx: String, label: String, location: String)
  object Loc {
    val home = "/"
    val about = "/about"
    val page3 = "/dyn/:foo(\\d+)/:bar(.*)"
  }
  val menuItems = Seq(
    MenuItem("0", "Home", Loc.home),
    MenuItem("1", "About", Loc.about),
    MenuItem("2", "Dynamic page", pathToPage3(678, "a/b/c"))
  )

  def pathToPage3(foo: Int, bar: String): String = {
    val compiled = PathToRegexp.compile(Loc.page3)
    compiled(
      js.Dynamic.literal(
        foo = foo,
        bar = bar
      ).asInstanceOf[PathToRegexp.ToPathData]
    )
  }
}
