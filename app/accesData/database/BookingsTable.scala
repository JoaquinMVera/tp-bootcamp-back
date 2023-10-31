package accesData.database

import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

import accesData.entities.Booking

import java.time.LocalDate

class BookingsTable(tag: Tag) extends Table[Booking](tag, "bookings") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def ticketsAmount = column[Int]("amount")

  def purchaseDate = column[LocalDate]("date")

  def performanceId: Rep[Long] = column[Long]("performance_id")
  def zoneId: Rep[Long] = column[Long]("zone_id")
  def userId: Rep[Long] = column[Long]("user_id")

  def performanceFK = foreignKey("book_fk_performance",performanceId,TableQuery[PerformancesTable])(_.id)

  def zoneFK = foreignKey("book_fk_zone",zoneId,TableQuery[ZonesTable])(_.id)

  def userFK = foreignKey("book_fk_user",userId,TableQuery[UsersTable])(_.id)

  override def * : ProvenShape[Booking] = (id,ticketsAmount,purchaseDate,performanceId,zoneId,userId) <> (Booking.tupled, Booking.unapply)

}

