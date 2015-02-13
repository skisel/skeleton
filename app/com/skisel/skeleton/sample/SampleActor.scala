package com.skisel.skeleton.sample

import akka.actor.Actor
import com.skisel.skeleton.ws.RequestResponseActor
import play.api.libs.json.Json

case class SampleRequest(id: String)
object SampleRequest {
  implicit val writes = Json.writes[SampleRequest]
  implicit val reads = Json.reads[SampleRequest]
}
case class SampleResponse(id: String)
object SampleResponse {
  implicit val writes = Json.writes[SampleResponse]
  implicit val reads = Json.reads[SampleResponse]
}

class SampleActor extends Actor with RequestResponseActor {
  override def receive: Receive = {
    case SampleRequest(id) =>
      response(SampleResponse(id))
  }
}
