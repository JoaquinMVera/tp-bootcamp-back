package controllers.writers

import accesData.entities.User
import play.api.libs.json.{Json, Writes}

object UserWriter {
  implicit val writesUser: Writes[User] = Json.writes[User]
}
