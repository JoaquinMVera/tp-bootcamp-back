package accesData.repositories

import accesData.database.{ ZonesTable}
import accesData.entities.{Zone}
import controllers.requests.ZoneRequest
import slick.dbio.{DBIO, _}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import syntax.errors.NotFoundError

import scala.concurrent.ExecutionContext.Implicits.global

class ZoneRepository {

  private val zonesTable = TableQuery[ZonesTable]


  def add(zoneRequest: ZoneRequest,showId: Long)  = {
    val zone = Zone(Int.MinValue, zoneRequest.name, zoneRequest.amount, zoneRequest.price, showId)
    val insertVenueQuery   = (zonesTable returning zonesTable) += zone
    insertVenueQuery
  }

  def findAll  = {
    val findAllQuery = zonesTable.result
    findAllQuery
  }

  def findById(zoneId: Long) = {
    val findByIdQuery = zonesTable.filter(_.id === zoneId).result.headOption.flatMap {
      case Some(user) => DBIO.successful(user)
      case None => DBIO.failed(NotFoundError(s"Not found an venue with $zoneId"))
    }
    findByIdQuery
  }

  def findByShowId(showId: Long) = {
    val findByShowId = zonesTable.filter(_.showId === showId).result
    findByShowId
  }
}