package accesData.repositories

import accesData.database.{RemainingTicketsTable}
import accesData.entities.{Performance, TicketsRemaining, Zone}
import slick.dbio.{DBIO}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import syntax.errors.NotFoundError

import scala.concurrent.ExecutionContext.Implicits.global

class RemainingTicketRepository {

  private val remaingTicketsTable = TableQuery[RemainingTicketsTable]


  def add(performance: Performance,zone: Zone)  = {

    val ticket = TicketsRemaining(Int.MinValue,zone.amount,zone.id,performance.id)

    val insertVenueQuery   = (remaingTicketsTable returning remaingTicketsTable) += ticket
    insertVenueQuery
  }

  def findAll  = {
    val findAllQuery = remaingTicketsTable.result
    DBIO.seq(findAllQuery)
  }

  def findById(ticketId: Long) = {
    val findByIdQuery = remaingTicketsTable.filter(_.id === ticketId).result.headOption.flatMap {
      case Some(remaining) => DBIO.successful(remaining)
      case None => DBIO.failed(NotFoundError(s"Not found a ticket remaining with $ticketId"))
    }
    findByIdQuery
  }

  def updateRemaining(ticketId: Long, amount: Int) = {
    val updateBalanceQuery = remaingTicketsTable.filter(_.id === ticketId).forUpdate.map(_.remaining).update(amount)
    updateBalanceQuery
  }

  def findByPerformanceAndZone(zoneId: Long,performanceId:Long) = {
    val findByQuery = remaingTicketsTable.filter(_.zoneId === zoneId).filter(_.performanceId === performanceId).forUpdate.result.headOption.flatMap{
      case Some(remaining) => DBIO.successful(remaining)
      case None => DBIO.failed(NotFoundError(s"Not found a ticket remaining with $zoneId zoneid and $performanceId performanceId"))
    }
    findByQuery
  }

}