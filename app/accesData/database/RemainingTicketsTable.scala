package accesData.database

import accesData.entities.{ Tickets}
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class RemainingTicketsTable(tag: Tag) extends Table[Tickets](tag, "remaining_tickets") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def remaining = column[Int]("amount_available")

  def performanceId: Rep[Long] = column[Long]("performance_id")

  def zoneId: Rep[Long] = column[Long]("zone_id")

  def performanceFK = foreignKey("remainings_fk_performance", performanceId, TableQuery[PerformancesTable])(_.id)

  def zoneFK = foreignKey("remainigs_fk_zone", zoneId, TableQuery[ZonesTable])(_.id)

  override def * : ProvenShape[Tickets] = (id,remaining,zoneId,performanceId) <> (Tickets.tupled, Tickets.unapply)

}
