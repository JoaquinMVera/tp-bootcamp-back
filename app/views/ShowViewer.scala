package views

import accesData.entities.{Performance, Show, Zone}
import controllers.requests.UserRequest
import play.api.libs.json.{Json, Reads, Writes}

case class ShowViewer(show: Show,performances: Seq[Performance],zones: Seq[Zone])


object ShowViewer {
  implicit val showWrites: Writes[Show] = Json.writes[Show]
  implicit val performanceWrites: Writes[Performance] = Json.writes[Performance]
  implicit val zoneWrites: Writes[Zone] = Json.writes[Zone]
  implicit val writesShowViewer: Writes[ShowViewer] = Json.writes[ShowViewer]
}

