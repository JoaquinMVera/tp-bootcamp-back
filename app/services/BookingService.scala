package services

import accesData.entities.{Performance, TicketsRemaining, User}
import accesData.repositories.{BookingRepository, PerformanceRepository, RemainingTicketRepository, UserRepository, ZoneRepository}
import controllers.requests.BookingRequest
import syntax.errors.RequestError
import slick.dbio.DBIO
import scala.concurrent._
import scala.concurrent.duration.DurationInt
import scala.util.{Try}
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api._
import accesData.DatabaseConfigModule


import javax.inject.Inject

class BookingService @Inject()(zoneRepository: ZoneRepository,
                               performanceRepository: PerformanceRepository,
                               remainingTicketRepository: RemainingTicketRepository, userRepository: UserRepository,
                               bookingRepository: BookingRepository) extends DatabaseConfigModule {


  def buyTickets(bookingRequest: BookingRequest) = {
    //Hay que validar primero el bookingRequest
    validateBookingRequest(bookingRequest).flatMap { _ =>
      val dbioTickets =
        for {
          user <- userRepository.findById(bookingRequest.userId)
          performance <- performanceRepository.findById(bookingRequest.performanceId)
          zone <- zoneRepository.findById(bookingRequest.zoneId)
          remainigs <- remainingTicketRepository.findByPerformanceAndZone(zone.id, performance.id)

          totalPrice = zone.price * bookingRequest.ticketAmount

          _ <- isActive(performance)
          _ <- canBuy(user, totalPrice)
          _ <- availableTickets(remainigs, bookingRequest.ticketAmount)


          booking <- bookingRepository.add(bookingRequest)
          _ <- userRepository.updateBalance(user.id, user.balance - totalPrice)
          _ <- remainingTicketRepository.updateRemaining(remainigs.id, remainigs.remaining - bookingRequest.ticketAmount) //ticktes del performance
        } yield booking

      Try(Await.result(db.run(dbioTickets.transactionally), 10.seconds)).toEither
    }

  }


  def getBookings(userId: Long) = {
    val dbioListBookings = for {
      _ <- userRepository.findById(userId)
      //me fijo que exista
      bookings <- bookingRepository.findByUserId(userId)
    } yield bookings
    Try(Await.result(db.run(dbioListBookings.transactionally), 10.seconds)).toEither

  }


  private def validateBookingRequest(bookingRequest: BookingRequest) = {
    bookingRequest match {
      //422, posible http error
      case _ if bookingRequest.ticketAmount <= 0 || bookingRequest.ticketAmount > 4 => Left(RequestError("Invalid ticket amount"))
      case _ => Right(())
    }
  }

  private def canBuy(user: User, amount: BigDecimal) = {
    user match {
      case _ if user.balance >= amount => DBIO.successful(())
      case _ => DBIO.failed(RequestError("The user balance is less than the buy price"))
    }
  }

  private def availableTickets(remainingTickets: TicketsRemaining, amountRequest: Int) = {
    remainingTickets match {
      case _ if remainingTickets.remaining >= amountRequest => DBIO.successful(())
      case _ => DBIO.failed(RequestError("The zone and the performance have less disponibility than the request"))
    }
  }

  private def isActive(performance: Performance) = {
    performance match {
      case _ if performance.active => DBIO.successful(())
      case _ => DBIO.failed(RequestError("The performance is inactive"))
    }
  }

}
