package accesData.entities

import java.sql.Date

case class Booking(id: Long,ticketAmount: Int,pucharseDate: Date,performanceId: Long, zoneId: Long, userId: Long)
