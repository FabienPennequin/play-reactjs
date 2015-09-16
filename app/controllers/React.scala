package controllers

import play.api._
import play.api.mvc._

import javax.inject._

import akka.actor._
import akka.pattern.ask

import js._
import js.RendererActor._

import play.twirl.api.Html
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._

@Singleton
class React @Inject() (system: ActorSystem) extends Controller {

  private lazy val renderer = system.actorOf(Props[RendererActor], "js-renderer")

  def browser = Action {
    Ok(views.html.react.browser())
  }

  def server = Action.async { implicit request =>
    val js = "React.renderToString(React.createElement(HelloMessage, {'name': 'John (from server)'}));"

    implicit val timeout = akka.util.Timeout(10.seconds)
    ((renderer ? ToRender(js)).map {
      case RenderedData(data) => Ok(views.html.react.server(Html(data)))
      case _ => InternalServerError("invalid response from js engine")
    }).recover {
      case _ => InternalServerError("Missing or invalid response from js engine")
    }
  }

}
