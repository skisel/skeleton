package com.skisel.skeleton.utils

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.skisel.skeleton.utils.Broadcaster._

class Broadcaster extends Actor with ActorLogging {
  var actors: Set[ActorRef] = Set()
  override def receive: Actor.Receive = {
    case AddListener(actor) => actors += actor
    case RemoveListener(actor) => actors -= actor
    case msg =>
      log.debug(s"Broadcasting message: $msg")
      actors.foreach(_ forward msg)
  }
}

object Broadcaster {
  case class AddListener(actor: ActorRef)
  case class RemoveListener(actor: ActorRef)
  def props = Props(classOf[Broadcaster])
}