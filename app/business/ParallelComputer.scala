package business

import akka.actor.ActorSystem
import javax.inject.Inject
import jp.co.bizreach.trace.TraceData
import utils.ZipkinKafkaTraceService

import scala.concurrent.{ExecutionContext, Future}

class ParallelComputer @Inject()(
    system: ActorSystem,
    val tracer: ZipkinKafkaTraceService)(implicit ec: ExecutionContext) {

  def compute(implicit parentTraceData: TraceData) =
    Future
      .sequence {
        (1 to 5)
          .map { i =>
            tracer.traceFuture("compute locally") { _ =>
              Future(i)
                .map(_ * 10)
                .map(_ + 1)
            }
          }
      }
      .map(_.sum)
}
