package accesData.entities

import play.api.libs.json.{Json, Writes}
import views.ShowViewer

case class Show(id: Long, name: String, category : String,venueId: Long)


object ShowWriter {
  implicit val writesShow: Writes[Show] = Json.writes[Show]
}

