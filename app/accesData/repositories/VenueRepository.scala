package accesData.repositories

import accesData.database.VenuesTable
import accesData.entities.Venue
import controllers.requests.VenueRequest
import slick.dbio.{DBIO, _}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import syntax.errors.NotFoundError

import scala.concurrent.ExecutionContext.Implicits.global

class VenueRepository {

  private val venuesTable = TableQuery[VenuesTable]


  def add(venueRequest: VenueRequest)  = {
    val venue = Venue(Int.MinValue,venueRequest.name,venueRequest.address,venueRequest.capacity)

    val insertVenueQuery   = (venuesTable returning (venuesTable)) += venue
    insertVenueQuery
  }

  def findAll  = {
    val findAllQuery = venuesTable.result
    findAllQuery
  }

  def findById(venueId: Long) = {
    val findByIdQuery = venuesTable.filter(_.id === venueId).result.headOption.flatMap {
      case Some(user) => DBIO.successful(user)
      case None => DBIO.failed(NotFoundError(s"Not found an venue with $venueId"))
    }
    findByIdQuery
  }

  //val join = Await.result(db.run(joinQuery.result), 10.seconds)
}
