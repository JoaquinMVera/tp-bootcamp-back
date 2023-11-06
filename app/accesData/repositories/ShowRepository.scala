package accesData.repositories

import accesData.database.{ShowsTable}
import accesData.entities.{Show}
import controllers.requests.ShowRequest
import slick.dbio.{DBIO, _}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import syntax.errors.NotFoundError
import scala.concurrent.ExecutionContext.Implicits.global


class ShowRepository {

  private val showsTable = TableQuery[ShowsTable]


  def add(showRequest: ShowRequest)  = {
    val show = Show(Int.MinValue,showRequest.name,showRequest.category,showRequest.venueId)
    val insertVenueQuery   = (showsTable returning showsTable) += show
   insertVenueQuery
  }

  def findAll  = {
    val findAllQuery = showsTable.result
    findAllQuery
  }

  def findById(showId: Long) = {
    val findByIdQuery = showsTable.filter(_.id === showId).result.headOption.flatMap {
      case Some(user) => DBIO.successful(user)
      case None => DBIO.failed(NotFoundError(s"Not found a show with $showId"))
    }
    findByIdQuery
  }


  //val join = Await.result(db.run(joinQuery.result), 10.seconds)
}
