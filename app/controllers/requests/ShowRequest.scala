package controllers.requests

import play.api.libs.json._

import java.time.LocalDate

case class ShowRequest(name:String,category:String,venueId: Long,performances: Seq[LocalDate],zones : List[ZoneRequest])

