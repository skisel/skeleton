package com.skisel.skeleton.sample

import akka.actor.{ActorLogging, Actor}
import com.skisel.skeleton.sample.Pinger.{PingEvent, Ping}
import com.skisel.skeleton.ws.OutMessage
import controllers.Application
import play.api.libs.json.Json


class Pinger extends Actor with ActorLogging{

  log info "Pinger started ..."

  import context.dispatcher
  import scala.concurrent.duration._

  context.system.scheduler.schedule(1.minute, 1.minute, self, Ping)

  override def receive: Receive = {
    case Ping =>
      log info "Sending ping..."
      Application.broadcaster ! OutMessage(PingEvent(System.currentTimeMillis()))
  }
}

object Pinger {

  case object Ping
  case class PingEvent(l: Long)

  object PingEvent {
    implicit val writes = Json.writes[PingEvent]
    implicit val reads = Json.reads[PingEvent]
  }

}
