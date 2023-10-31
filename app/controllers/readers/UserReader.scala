package controllers.readers

import controllers.requests.UserRequest
import play.api.libs.json.{Json, Reads}

object UserReader {
  implicit val readsUser: Reads[UserRequest] = Json.reads[UserRequest]

}
