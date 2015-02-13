package com.skisel.skeleton.ws

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill}
import com.skisel.skeleton.ws.RequestResponseActor.{Response, Request}
import play.api.libs.json.Json

case class ResponseFailure(error: String)
object ResponseFailure {
  implicit val reads = Json.reads[ResponseFailure]
  implicit val writes = Json.writes[ResponseFailure]
}

object RequestResponseActor {
  case class Request(id: String, anyMsg: Any)
  case class Response(id: String, anyMsg: Either[AnyRef, ResponseFailure])
}
trait RequestResponseActor extends Actor with ActorLogging {

  private var requestId: Option[String] = None
  private var requester: Option[ActorRef] = None
  var requestSent: Boolean = false

  override def aroundReceive(receive: Receive, msg: Any): Unit = {
    msg match {
      case Request(messageId, message) =>
        requestId = Some(messageId)
        requester = Some(sender())
        super.aroundReceive(receive, message)
      case anyMessage =>
        super.aroundReceive(receive, anyMessage)
    }
  }

  def response(anyMsg: AnyRef) = {
    respond(Left(anyMsg))
    self ! PoisonPill
    requestSent = true
  }

  def failure(failedResponse: ResponseFailure) = {
    respond(Right(failedResponse))
    self ! PoisonPill
    requestSent = true
  }

  private def respond(res: Either[AnyRef, ResponseFailure]) = {
    (requestId, requester) match {
      case (Some(messageId), Some(actor)) =>
        log.debug(s"sending failure response with $messageId : $res")
        actor ! OutMessage(Response(messageId, res))
      case (_, _) =>
        log.error(s"no message id or actor $requestId, $requester")
    }
  }

  override def postStop(): Unit = {
    if (!requestSent) {
      (requestId, requester) match {
        case (Some(messageId), Some(actor)) =>
          log.debug(s"sending failure response with $messageId")
          actor ! OutMessage(Response(messageId, Right(ResponseFailure("request timed out"))))
        case (_, _) =>
          log.error(s"no message id or actor $requestId, $requester")
      }
    }
  }
}