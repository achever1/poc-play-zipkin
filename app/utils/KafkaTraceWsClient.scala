package utils

import javax.inject.Inject
import jp.co.bizreach.trace.play.TraceWSClient
import play.api.libs.ws.WSClient

class KafkaTraceWsClient @Inject()(wsClient: WSClient,
                                   tracer: ZipkinKafkaTraceService)
    extends TraceWSClient(wsClient, tracer)
