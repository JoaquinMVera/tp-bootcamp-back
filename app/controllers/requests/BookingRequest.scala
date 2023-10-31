package controllers.requests

case class BookingRequest(userId: Long, zoneId: Long, performanceId: Long, ticketAmount: Int)
