package controllers.writers

import accesData.entities.Booking
import play.api.libs.json.{Json, Writes}

object BookingWriter {
  implicit val writesPerformance: Writes[Booking] = Json.writes[Booking]
}
