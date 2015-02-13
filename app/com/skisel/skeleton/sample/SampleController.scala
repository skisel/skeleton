package com.skisel.skeleton.sample

import akka.actor.Props
import play.libs.Akka

trait SampleController {
  val pinger = Akka.system().actorOf(Props[Pinger])


}
