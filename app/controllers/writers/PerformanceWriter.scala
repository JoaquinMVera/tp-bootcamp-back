package controllers.writers

import accesData.entities.Performance
import play.api.libs.json.{Json, Writes}

object PerformanceWriter {
  implicit val writesPerformance: Writes[Performance] = Json.writes[Performance]

}
