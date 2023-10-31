package controllers.readers

import controllers.requests.{DepositRequest, VenueRequest}
import play.api.libs.json.{Json, Reads}

object DepositReader {
  implicit val readsDeposit: Reads[DepositRequest] = Json.reads[DepositRequest]
}
