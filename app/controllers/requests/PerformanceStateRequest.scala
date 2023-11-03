package controllers.requests

import play.api.libs.json._


case class PerformanceStateRequest(performanceId: Long,state: Boolean)


object PerformanceStateRequest {
  implicit val readsShow: Reads[PerformanceStateRequest] = Json.reads[PerformanceStateRequest]
}


