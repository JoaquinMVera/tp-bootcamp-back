package accesData.database

import accesData.entities.Performance
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

import java.sql.Date

class PerformancesTable(tag: Tag) extends Table[Performance](tag, "performances") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def date = column[Date]("date")

  def active = column[Boolean]("active")

  def showId: Rep[Long] = column[Long]("id_espectaculo")

  def showFK = foreignKey("fk_espectaculo",showId,TableQuery[ShowsTable])(_.id)

  override def * : ProvenShape[Performance] = (id,date,active,showId) <> (Performance.tupled, Performance.unapply)

}

