package accesData.database

import accesData.entities.Zone

import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class ZonesTable(tag: Tag) extends Table[Zone](tag, "zones") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def amount = column[Int]("amount")

  def price = column[BigDecimal]("price")

  def showId = column[Long]("id_performance")


  def showID = foreignKey("zones_fk_show", showId, TableQuery[PerformancesTable])(_.id)

  override def * : ProvenShape[Zone] = (id, name, amount,price,showId) <> (Zone.tupled, Zone.unapply)

}
