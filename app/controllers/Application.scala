package controllers

import akka.actor._
import com.skisel.skeleton.sample.SampleController
import com.skisel.skeleton.ws.{ConnectionActor, ApplicationController}
import org.webjars.RequireJS
import play.api.mvc.Action

object Application extends ApplicationController(classOf[SpecificConnectionActor])
  with SampleController {

  def setup = Action {
    Ok(RequireJS.getSetupJavaScript("/webjars/")).as("text/javascript")
  }

  def index = Action {
    Found("/assets/index.html")
  }

}

class SpecificConnectionActor(val out: ActorRef, val broadcaster: ActorRef) extends ConnectionActor(out, broadcaster)
