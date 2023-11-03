package accesData.repositories

import accesData.database.{BookingsTable}
import accesData.entities.{Booking}
import controllers.requests.BookingRequest
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate


class BookingRepository {

  private val bookingsTable = TableQuery[BookingsTable]


  def add(bookingRequest: BookingRequest) = {
    val booking = Booking(Int.MinValue, bookingRequest.ticketAmount,
      LocalDate.now(), bookingRequest.performanceId, bookingRequest.zoneId, bookingRequest.userId)
    val insertVenueQuery = (bookingsTable returning bookingsTable) += booking
    insertVenueQuery
  }

  def findAll = {
    val findAllQuery = bookingsTable.result
    findAllQuery
  }

  def findById(bookingId: Long) = {
    val findByIdQuery = bookingsTable.filter(_.id === bookingId).result
    findByIdQuery
  }

  def findByUserId(userId: Long) = {
    val findByUserIdQuery = bookingsTable.filter(_.userId === userId).result
    findByUserIdQuery
  }

}