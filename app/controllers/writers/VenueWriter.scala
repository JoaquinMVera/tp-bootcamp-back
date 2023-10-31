package controllers.writers

import accesData.entities.{User, Venue}
import play.api.libs.json.{Json, Writes}

object VenueWriter {
  implicit val writesVenue: Writes[Venue] = Json.writes[Venue]
}
