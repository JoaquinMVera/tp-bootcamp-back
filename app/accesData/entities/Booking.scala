package accesData.entities

import java.time.LocalDate

case class Booking(id: Long, ticketsAmount: Int, purchaseDate: LocalDate, performanceId: Long, zoneId: Long, userId: Long)
