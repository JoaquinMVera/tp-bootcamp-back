package controllers.validators

import controllers.errors.InvalidBody
import controllers.requests.{DepositRequest, ShowRequest}
import play.api.libs.json.JsValue
import controllers.readers.DepositReader.readsDeposit

object DepositValidator {

  def validate(jsonBody: Option[JsValue]): Either[Throwable, Unit] = {
    jsonBody match {
      case None => Left(InvalidBody("body with invalid format for a JSON"))
      case Some(json) =>
        val showOption = json.asOpt[DepositRequest]
        showOption match {
          case None => Left(InvalidBody("invalid format in the json"))
          case Some(show) =>
            //Si llegue hasta aca, el cuerpo era valido!
            Right(())
        }

    }

  }
}
