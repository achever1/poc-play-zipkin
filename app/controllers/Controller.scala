package controllers

import javax.inject._
import jp.co.bizreach.trace.ZipkinTraceServiceLike
import jp.co.bizreach.trace.play.TraceWSClient
import jp.co.bizreach.trace.play.implicits.ZipkinTraceImplicits
import play.api.libs.ws.ahc.AhcCurlRequestLogger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class Controller @Inject()(
    cc: ControllerComponents,
    ws: TraceWSClient,
    val tracer: ZipkinTraceServiceLike)(implicit ec: ExecutionContext)
    extends AbstractController(cc)
    with ZipkinTraceImplicits {

  def endpoint1() = Action.async { implicit request =>
    ws.url("rest client call", "http://s2:9000/endpoint2")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint2 replied"))
  }

  def endpoint2 = Action.async { implicit request =>
    ws.url("rest client call", "http://s3:9000/endpoint3")
      .withRequestFilter(AhcCurlRequestLogger())
      .get()
      .map(_ => Ok("endpoint3 replied"))
  }

  def endpoint3() = Action.async { implicit request =>
    Thread.sleep(1000)
    Future(Ok("called by called !"))
  }

}
