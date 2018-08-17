package controllers

import actors.{ComputeCommand, ParallelComputerActor}
import akka.actor.{ActorSystem, Props}
import business.ParallelComputer
import javax.inject._
import jp.co.bizreach.trace.ZipkinTraceServiceLike
import jp.co.bizreach.trace.akka.actor.ActorTraceSupport.TraceableActorRef
import jp.co.bizreach.trace.play.TraceWSClient
import jp.co.bizreach.trace.play.implicits.ZipkinTraceImplicits
import play.api.libs.ws.ahc.AhcCurlRequestLogger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class Controller @Inject()(
    cc: ControllerComponents,
    ws: TraceWSClient,
    parallelComputer: ParallelComputer,
    system: ActorSystem
)(implicit ec: ExecutionContext, val tracer: ZipkinTraceServiceLike)
    extends AbstractController(cc)
    with ZipkinTraceImplicits {

  val props = Props(new ParallelComputerActor(parallelComputer, tracer))
  val actor = TraceableActorRef(system.actorOf(props, "ComputerActor"))

  def endpoint1() = Action.async { implicit request =>
    ws.url("rest client call", "http://s2:9000/endpoint2")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint2 replied"))
  }

  def endpoint2 = Action.async { implicit request =>

    // Parallel computation with futures
    tracer.traceFuture("Compute globally") { _ =>
      parallelComputer.compute
    }

    // Parallel computation delegated to an actor
    actor ! ComputeCommand()

    ws.url("rest client call", "http://s3:9000/endpoint3")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint3 replied"))
  }

  def endpoint3() = Action.async { implicit request =>
    tracer.trace("Waiting thread 100 ms") { _ => Thread.sleep(100) }
    Future(Ok("endpoint3 sending reply"))
  }

}
