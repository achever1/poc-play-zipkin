package actors

import jp.co.bizreach.trace.akka.actor.ActorTraceSupport.{ActorTraceData, TraceMessage}

case class ComputeCommand()(implicit val traceData: ActorTraceData) extends TraceMessage
