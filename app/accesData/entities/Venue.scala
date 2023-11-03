package accesData.entities

import play.api.libs.json.{Json, Writes}

case class Venue(id: Long, name: String, address: String,capacity: Int)

object VenueWriter {
  implicit val writesVenue: Writes[Venue] = Json.writes[Venue]
}

