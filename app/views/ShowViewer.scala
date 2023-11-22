package views

import accesData.entities.{Performance, Show, Zone,TicketsRemaining}
import controllers.requests.UserRequest
import play.api.libs.json.{Json, Reads, Writes}


case class ShowViewer(show: Show,performances: Seq[Performance],zones: Seq[Zone],remainingTickets:Seq[TicketsRemaining],soldOut: Boolean)


object ShowViewer {
  implicit val ticketWrites: Writes[TicketsRemaining] = Json.writes[TicketsRemaining]
  implicit val showWrites: Writes[Show] = Json.writes[Show]
  implicit val performanceWrites: Writes[Performance] = Json.writes[Performance]
  implicit val zoneWrites: Writes[Zone] = Json.writes[Zone]
  implicit val writesShowViewer: Writes[ShowViewer] = Json.writes[ShowViewer]
}

