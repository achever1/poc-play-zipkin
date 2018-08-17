package actors

import akka.actor.Actor
import business.ParallelComputer
import javax.inject.Inject
import jp.co.bizreach.trace.akka.actor.ActorTraceSupport.TraceableActor
import jp.co.bizreach.trace.play.implicits.ZipkinTraceImplicits
import jp.co.bizreach.trace.{TraceData, ZipkinTraceServiceLike}

class ParallelComputerActor @Inject()(computer: ParallelComputer,
                                      val tracer: ZipkinTraceServiceLike)
    extends Actor
    with TraceableActor
    with ZipkinTraceImplicits {

  def receive = {
    case cmd: ComputeCommand ⇒
      tracer.traceFuture("compute globally") { implicit traceData =>
        computer.compute
      }(TraceData(cmd.traceData.span)) // FIXME a bit silly to transform manually ActorTraceData to TraceData
  }
}
