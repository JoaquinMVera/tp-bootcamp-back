package controllers.requests

import play.api.libs.json.{Json, Reads}

case class BookingRequest(userId: Long, zoneId: Long, performanceId: Long, ticketAmount: Int)

object BookingRequest {
  implicit val readsUser: Reads[BookingRequest] = Json.reads[BookingRequest]

}
