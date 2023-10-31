package controllers.readers

import controllers.requests.VenueRequest
import play.api.libs.json.{Json, Reads}

object VenueReader {

    implicit val readsVenue: Reads[VenueRequest] = Json.reads[VenueRequest]

}
