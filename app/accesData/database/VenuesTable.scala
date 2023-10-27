package accesData.database

import accesData.entities.Venue
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class VenuesTable(tag: Tag) extends Table[Venue](tag, "venues") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def address = column[String]("address")

  def capacity = column[Int]("capacity")

  override def * : ProvenShape[Venue] = (id, name,address,capacity) <> (Venue.tupled, Venue.unapply)

}
