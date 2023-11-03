package controllers.requests

import play.api.libs.json._

import java.time.LocalDate

case class ShowRequest(name:String,category:String,venueId: Long,performances: Seq[LocalDate],zones : Seq[ZoneRequest])

object ShowRequest {
  implicit val zoneRequestReads: Reads[ZoneRequest] = Json.reads[ZoneRequest]
  implicit val readsShow: Reads[ShowRequest] = Json.reads[ShowRequest]
}


