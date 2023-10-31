package controllers.writers

import accesData.entities.Show
import play.api.libs.json.{Json, Writes}

object ShowWriter {
  implicit val writes: Writes[Show] = Json.writes[Show]
}
