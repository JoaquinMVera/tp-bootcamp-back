package accesData.database

import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

import java.sql.Date
import accesData.entities.Booking

class BookingsTable(tag: Tag) extends Table[Booking](tag, "performances") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def ticketAmount = column[Int]("amount")

  def pucharseDate = column[Date]("date")

  def performanceId: Rep[Long] = column[Long]("performance_id")
  def zoneId: Rep[Long] = column[Long]("zone_id")
  def userId: Rep[Long] = column[Long]("user_id")

  def performanceFK = foreignKey("fk_performance",performanceId,TableQuery[PerformancesTable])(_.id)

  def zoneFK = foreignKey("fk_zone",zoneId,TableQuery[ZonesTable])(_.id)

  def userFK = foreignKey("fk_user",userId,TableQuery[UsersTable])(_.id)

  override def * : ProvenShape[Booking] = (id,ticketAmount,pucharseDate,performanceId,zoneId,userId) <> (Booking.tupled, Booking.unapply)

}

