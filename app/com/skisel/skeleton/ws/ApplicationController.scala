package com.skisel.skeleton.ws

import akka.actor.Props
import com.skisel.skeleton.utils.Broadcaster
import play.api.libs.json.JsValue
import play.api.mvc.WebSocket.FrameFormatter
import play.api.mvc.{Controller, WebSocket}
import play.libs.Akka


class ApplicationController(clazz: Class[_]) extends Controller{
  import play.api.Play.current

  val broadcaster = Akka.system().actorOf(Broadcaster.props)

  implicit val incomingFormatter = FrameFormatter.jsonFrame.transform[InMessage](
    out => out.json,
    in => InMessage(in)
  )

  def ws = WebSocket.acceptWithActor[InMessage, JsValue] {
    request => out => Props(clazz, out, broadcaster)
  }


}
