package accesData.repositories

import accesData.database.{PerformancesTable}
import accesData.entities.{Performance}
import controllers.requests.PerformanceStateRequest
import slick.dbio.{DBIO, _}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import syntax.errors.NotFoundError

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global

class PerformanceRepository {

  private val performancesTable = TableQuery[PerformancesTable]


  def add(date: LocalDate,showId: Long)  = {
    val performance = Performance(Int.MinValue, date, true, showId)
    val insertVenueQuery   = (performancesTable returning performancesTable) += performance
    insertVenueQuery
  }

  def findAll  = {
    val findAllQuery = performancesTable.result
    findAllQuery
  }

  def findById(performanceId: Long) = {
    val findByIdQuery = performancesTable.filter(_.id === performanceId).result.headOption.flatMap {
      case Some(performance) => DBIO.successful(performance)
      case None => DBIO.failed(NotFoundError(s"Not found a performance with $performanceId"))
    }
    findByIdQuery
  }

  def findByShowId(showId: Long) = {
    val findByShowId = performancesTable.filter(_.showId === showId).result
    findByShowId
  }

  def updateActive(performanceStateRequest: PerformanceStateRequest) = {
    val updateActiveQuery = performancesTable.filter(_.id === performanceStateRequest.performanceId).map(_.active).update(performanceStateRequest.state)
    updateActiveQuery

  }

  //val join = Await.result(db.run(joinQuery.result), 10.seconds)
}
