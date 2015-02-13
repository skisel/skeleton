package com.skisel.skeleton.ws

import akka.actor._
import com.skisel.skeleton.sample.Pinger.PingEvent
import com.skisel.skeleton.sample.{SampleResponse, SampleActor, SampleRequest}
import com.skisel.skeleton.utils.Broadcaster
import Broadcaster.{RemoveListener, AddListener}
import com.skisel.skeleton.ws.RequestResponseActor.{Response, Request}
import play.api.libs.json.{Json, JsValue}


case class InMessage(json: JsValue)
case class OutMessage(message: AnyRef)
case class Clean(actorRef: ActorRef)

class ConnectionActor(out: ActorRef, broadcaster: ActorRef)
    extends Actor
    with ActorLogging{

  var actors: Set[ActorRef] = Set()
  var schedulers: Set[Cancellable] = Set()

  override def preStart(): Unit = broadcaster.tell(AddListener(self), Actor.noSender)

  override def postStop(): Unit = {
    broadcaster.tell(RemoveListener(self), Actor.noSender)
    schedulers.foreach {
      _.cancel()
    }
  }

  def receive = {

    case InMessage(json) =>
      log.info(s">>> json: ${json.toString()}")

      json match {
        case json: JsValue if (json \ "type").as[String] == "sample-incoming" =>
          val args = json \ "args"
          val idOpt = (json \ "requestId").asOpt[String]
          val data = args.as[SampleRequest]
          val actor = context.actorOf(Props[SampleActor])

          context.watch(actor)

          val idz: Option[String] = idOpt

          idz match {
            case Some(id) => actor ! Request(id, data)
            case None => actor ! data
          }

          import context.dispatcher

          import scala.concurrent.duration._
          schedulers += context.system.scheduler.scheduleOnce(30 seconds, self, Clean(actor))
        case _ =>
          log info "Invalid request"
      }


    case OutMessage(message) =>

      message match {
        case message: PingEvent =>
          Json.obj(
            "type" -> "ping-event",
            "args" -> Json.toJson(message)
          )

        case Response(requestId, Left(message: SampleResponse)) =>
          Json.obj(
            "type" -> "sample-outgoing",
            "requestId" ->  requestId,
            "args" -> Json.toJson(message)
          )

        case Response(requestId, Right(ResponseFailure(errorMsg))) =>
          Json.obj(
            "type" -> "sample-outgoing",
            "requestId" ->  requestId,
            "errMsg" -> errorMsg
          )
      }

    case Clean(actor) if actors.contains(actor) =>
      actor ! PoisonPill
    case Clean(_) => //was cleaned already
    case Terminated(actor) =>
      actors -= actor
    case other =>
      throw new RuntimeException(s"Could not match: $other")
  }

}