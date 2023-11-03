package accesData.entities

import play.api.libs.json.{Json, Writes}

import java.time.LocalDate

case class Performance(id: Long,date: LocalDate,active: Boolean,showId: Long)

object PerformanceWriter {
  implicit val writesPerformance: Writes[Performance] = Json.writes[Performance]

}




