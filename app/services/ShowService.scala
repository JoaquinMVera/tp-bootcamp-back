package services

import accesData.entities.{Performance, Show, Venue, Zone}
import accesData.repositories.{PerformanceRepository, RemainingTicketRepository, ShowRepository, VenueRepository, ZoneRepository}
import controllers.requests.{PerformanceStateRequest, ShowRequest, ZoneRequest}
import syntax.errors.RequestError
import slick.dbio.DBIO

import java.time.LocalDate
import scala.concurrent._
import scala.concurrent.duration.DurationInt
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global
import slick.jdbc.PostgresProfile.api._
import accesData.DatabaseConfigModule
import views.{ShowAllViewer, ShowViewer}

import javax.inject.Inject

class ShowService @Inject()(showRepository: ShowRepository, zoneRepository: ZoneRepository,
                            performanceRepository: PerformanceRepository, venueRepository: VenueRepository,
                            remainingTicketRepository: RemainingTicketRepository) extends DatabaseConfigModule {
  //@inject que te lo hace solo,preguntarle bien a joaco


  //Esto de arriba hay que sacarlo, meterlo en una inyeccion de dependencias

  def add(showRequest: ShowRequest): Either[Throwable, Show] = {

    validateShowRequest(showRequest).flatMap { _ =>
      val dbioShow = for {

        venue <- venueRepository.findById(showRequest.venueId)
        _ <- validateZonesCapacity(showRequest, venue)
        show <- showRepository.add(showRequest)


        performances <- DBIO.sequence(
          showRequest.performances.map {
            date => performanceRepository.add(date, show.id)
          }
        )

        zones <- DBIO.sequence(
          showRequest.zones.map {
            zoneRequest => zoneRepository.add(zoneRequest, show.id)
          }
        )

        _ <- DBIO.sequence {
          for {
            performance <- performances
            zone <- zones
          } yield remainingTicketRepository.add(performance, zone)

        }


      } yield show

      Try(Await.result(db.run(dbioShow.transactionally), 10.seconds)).toEither


    }

  }

  def getAllShows = {
    val dbioShowAll = for {
      shows <- showRepository.findAll
    } yield shows

  Try(Await.result(db.run(dbioShowAll),10.seconds)).toEither

  }

  def getShowViewerById(showId: Long) = {
    val dbioShowViewer =
      for {
        show <- showRepository.findById(showId)
        performances <- performanceRepository.findByShowId(showId)
        zones <- zoneRepository.findByShowId(showId)
        remainings <- DBIO.sequence(for {
          performance <- performances
          zone <- zones
        } yield remainingTicketRepository.findByPerformanceAndZone(zone.id,performance.id))

      } yield ShowViewer(show, performances, zones,remainings,remainings.forall(_.remaining == 0))

    Try(Await.result(db.run(dbioShowViewer), 10.seconds)).toEither

  }


  def changePerformanceState(showId: Long, performanceStateRequest: PerformanceStateRequest): Either[Throwable, Performance] = {
    val dbioPerformance =
      for {
        show <- showRepository.findById(showId)
        _ <- performanceRepository.findById(performanceStateRequest.performanceId)
        _ <- performanceRepository.updateActive(performanceStateRequest)
        performance <- performanceRepository.findById(performanceStateRequest.performanceId)
      } yield performance

    Try(Await.result(db.run(dbioPerformance), 10.seconds)).toEither

  }


  //Lo mismo que en las venues, esto podria ser otro archivo
  private def validateZonesCapacity(showRequest: ShowRequest, venue: Venue) = {
    showRequest.zones.map {
      zone => zone.amount
    }.sum match {
      case suma if suma > venue.capacity => DBIO.failed(RequestError("The show zones capacity must be less than the venue capacity"))
      case _ => DBIO.successful(())
    }
  }

  private def validateShowRequest(showRequest: ShowRequest): Either[Throwable, Unit] = {
    showRequest match {
      case _ if showRequest.name.isEmpty => Left(RequestError("The show name is empty"))
      case _ if showRequest.category.isEmpty => Left(RequestError("The show category is empty"))
      case _ if !isValidCategory(showRequest.category) => Left(RequestError("The show category is invalid"))
      case _ if showRequest.zones.isEmpty => Left(RequestError("The show zones is empty"))
      case _ if showRequest.performances.isEmpty => Left(RequestError("The show dates is empty"))
      case _ if showRequest.performances.exists(date => date.isBefore(LocalDate.now)) => Left(RequestError("The show dates must be in the future"))

      //Esto es medio tricky porque hay que hacer como dos veces lo mismo, pero validamos que cada zoneRequest sea valida(nada empty, nada negativo)
      case _ if showRequest.zones.exists(zone => validateZoneRequest(zone).isLeft) =>
        showRequest.zones.map(zone => validateZoneRequest(zone)).find(_.isLeft).get


      case _ => Right(())
    }
  }


  private def validateZoneRequest(zone: ZoneRequest): Either[Throwable, Unit] = {
    zone match {
      case _ if zone.name.isEmpty => Left(RequestError("The zones name is empty"))
      case _ if zone.amount <= 0 => Left(RequestError(s"The ${zone.name} ticket amount is less than 0"))
      case _ if zone.price < 0 => Left(RequestError(s"The ${zone.name} price is less than 0"))
      case _ => Right(())
    }
  }


  // Musical, concierto y teatro
  private def isValidCategory(category: String) = {
    category match {
      case "musical" => true
      case "concert" => true
      case "theater" => true
      case _ => false
    }
  }
}
