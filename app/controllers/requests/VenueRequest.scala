package controllers.requests

import play.api.libs.json.{Json, Reads}

case class VenueRequest(name: String, address : String, capacity: Int)

