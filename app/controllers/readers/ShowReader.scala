package controllers.readers

import controllers.requests.{ShowRequest, ZoneRequest}
import play.api.libs.json.{Json, Reads}

object ShowReader {
  implicit val zoneRequestReads: Reads[ZoneRequest] = Json.reads[ZoneRequest]
  implicit val readsShow: Reads[ShowRequest] = Json.reads[ShowRequest]
}
