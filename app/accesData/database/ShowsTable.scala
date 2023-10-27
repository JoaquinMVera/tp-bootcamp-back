package accesData.database

import accesData.entities.Show
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class ShowsTable(tag: Tag) extends Table[Show](tag, "shows") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def category = column[String]("category")

  def venueId = column[Long]("id_venue")

  override def * : ProvenShape[Show] = (id, name, category,venueId) <> (Show.tupled, Show.unapply)

}
