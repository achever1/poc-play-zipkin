package utils

import akka.stream.Materializer
import javax.inject.Inject
import jp.co.bizreach.trace.play.filter.ZipkinTraceFilter


class ZipkinKafkaTraceFilter @Inject()(tracer: ZipkinKafkaTraceService)(
  implicit override val mat: Materializer)
  extends ZipkinTraceFilter(tracer)
