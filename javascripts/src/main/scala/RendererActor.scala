package js

import akka.actor._
import javax.script._

import RendererActor._

class RendererActor extends Actor with ActorLogging {

  private val engine: ScriptEngine = createNashorn

  def receive = {
    case _@ToRender(js) => {
      val replyTo = sender()
      log.debug(s"Request for rendering js: $js")
      // try {
        val data = engine.eval(js).toString()
        replyTo ! RenderedData(data)
      // } catch {
      //   case e: Exception =>
      //     log.error(e.getMessage)
      //     replyTo ! RenderError(e)
      // }
    }

    case _ =>
  }

  private def createNashorn = {
    log.info(s"Starting to intialize nashorn...")

    val nashorn = new ScriptEngineManager(null).getEngineByName("nashorn")
    if (nashorn == null) {
      log.error(s"Unable to find nashorn javascript engine (jdk8?)")
      throw new RuntimeException("Missing nashorn javascript engine")
      // self ! PoisonPill
    }

    nashorn.eval("var global = this;")
    nashorn.eval("var console = {error: print, log: print, warn: print};")

    nashorn.eval(Resources.reactjs)
    nashorn.eval(Resources.server)

    log.info(s"Nashorn initialized with success")

    nashorn
  }

}

object RendererActor {
  case class ToRender(js: String)
  case class RenderedData(data: String)
  case class RenderError(e: Exception)
}
