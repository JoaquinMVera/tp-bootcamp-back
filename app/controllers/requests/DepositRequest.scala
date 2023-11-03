package controllers.requests

import play.api.libs.json.{Json, Reads}

case class DepositRequest(amount: BigDecimal)


object DepositRequest {

  implicit val readsDeposit: Reads[DepositRequest] = Json.reads[DepositRequest]

}
