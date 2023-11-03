package services

import accesData.entities.Venue
import accesData.repositories.VenueRepository
import controllers.requests.VenueRequest
import syntax.errors.{RequestError}

import javax.inject.Inject
import scala.concurrent._
import scala.concurrent.duration.DurationInt
import scala.util.{Try}
import accesData.DatabaseConfigModule
import scala.concurrent.ExecutionContext.Implicits.global


class VenueService @Inject()(venueRepository: VenueRepository) extends DatabaseConfigModule {


    def addVenue(venueRequest: VenueRequest) : Either[Throwable,Venue] = {
        validateVenueRequest(venueRequest).flatMap{ _ =>
            val dbioVenue = for {
                venue <- venueRepository.add(venueRequest)
            } yield venue

            Try(Await.result(db.run(dbioVenue),10.seconds)).toEither
        }
    }

    def getAllVenues : Either[Throwable,Seq[Venue]] = {
         Try(Await.result(db.run(venueRepository.findAll),10.seconds)).toEither
    }


    private def validateVenueRequest(venueRequest: VenueRequest) : Either[Throwable,Unit] = {
        venueRequest match {
            case _ if venueRequest.name.isEmpty => Left(RequestError("The venue name is empty"))
            case _ if venueRequest.address.isEmpty => Left(RequestError("The venue address is empty"))
            case _ if venueRequest.capacity <= 0 => Left(RequestError("The venue capacity must be more than 0"))
            case _ => Right(())
        }
    }
}
