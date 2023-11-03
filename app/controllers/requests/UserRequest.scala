package controllers.requests

import play.api.libs.json.{Json, Reads}

case class UserRequest(name: String, email: String)

object UserRequest {
  implicit val readsUser: Reads[UserRequest] = Json.reads[UserRequest]

}




