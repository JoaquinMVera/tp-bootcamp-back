package accesData.entities

import play.api.libs.json.{Json, Writes}

import java.time.LocalDate

case class Booking(id: Long, ticketsAmount: Int, purchaseDate: LocalDate, performanceId: Long, zoneId: Long, userId: Long)


object BookingWriter {
  implicit val writesPerformance: Writes[Booking] = Json.writes[Booking]
}
