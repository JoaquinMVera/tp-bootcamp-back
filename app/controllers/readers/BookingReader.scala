package controllers.readers

import controllers.requests.{BookingRequest}
import play.api.libs.json.{Json, Reads}

object BookingReader {
  implicit val readsUser: Reads[BookingRequest] = Json.reads[BookingRequest]

}
