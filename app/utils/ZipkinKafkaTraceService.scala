package utils

import akka.actor.ActorSystem
import brave.Tracing
import brave.sampler.Sampler
import javax.inject.Inject
import jp.co.bizreach.trace.{ZipkinTraceConfig, ZipkinTraceServiceLike}
import play.api.Configuration
import zipkin2.Span
import zipkin2.reporter.kafka11.KafkaSender
import zipkin2.reporter.{AsyncReporter, Reporter}

import scala.concurrent.ExecutionContext

class ZipkinKafkaTraceService @Inject()(
    conf: Configuration,
    actorSystem: ActorSystem
) extends ZipkinTraceServiceLike {

  implicit val executionContext: ExecutionContext =
    actorSystem.dispatchers.lookup(ZipkinTraceConfig.AkkaName)

  val tracing: Tracing = Tracing
    .newBuilder()
    .localServiceName(serviceName)
    .spanReporter(reporter)
    .sampler(sampleRate)
    .build()

  // Defines the sampler
  lazy val sampleRate: Sampler =
    conf
      .getOptional[String](ZipkinTraceConfig.ZipkinSampleRate)
      .map(s => Sampler.create(s.toFloat))
      .getOrElse(Sampler.ALWAYS_SAMPLE)

  // Defines the service name
  lazy val serviceName: String =
    conf
      .getOptional[String](ZipkinTraceConfig.ServiceName)
      .getOrElse("unknown")

  // Defines the kafka reporter
  lazy val reporter: Reporter[Span] =
    AsyncReporter.create(
      KafkaSender
        .newBuilder()
        .bootstrapServers(
          conf
            .getOptional[String](ZipkinKafkaTraceService.ZipkinBaseUrl)
            .getOrElse(""))
        .topic(
          conf
            .getOptional[String](ZipkinKafkaTraceService.ZipkinTopic)
            .getOrElse("zipkin"))
        .build()
    )
}

object ZipkinKafkaTraceService {
  val ZipkinBaseUrl = "trace.zipkin.kafka-url"
  val ZipkinTopic = "trace.zipkin.topic"
}
