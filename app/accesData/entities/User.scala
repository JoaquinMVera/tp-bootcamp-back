package accesData.entities

import play.api.libs.json.{Json, Writes}

case class User(id: Long, name: String, email: String, balance: BigDecimal)

object UserWriter {
  implicit val writesUser: Writes[User] = Json.writes[User]
}

