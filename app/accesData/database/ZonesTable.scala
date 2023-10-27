package accesData.database

import accesData.entities.Zone

import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class ZonesTable(tag: Tag) extends Table[Zone](tag, "zonas") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("nombre")

  def amount = column[Int]("cantidad")

  def price = column[Int]("precio")

  def performanceId = column[Long]("id_funcion")


  def direccionFk = foreignKey("fk_funcion", performanceId, TableQuery[PerformancesTable])(_.id)

  override def * : ProvenShape[Zone] = (id, name, amount,price,performanceId) <> (Zone.tupled, Zone.unapply)

}
