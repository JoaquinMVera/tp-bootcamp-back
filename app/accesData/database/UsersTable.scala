package accesData.database

import accesData.entities.User
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.H2Profile.api._

class UsersTable(tag: Tag) extends Table[User](tag, "users") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def email = column[String]("email")

  def balance  = column[BigDecimal]("balance")

  override def * : ProvenShape[User] = (id, name, email,balance) <> (User.tupled, User.unapply)

}
