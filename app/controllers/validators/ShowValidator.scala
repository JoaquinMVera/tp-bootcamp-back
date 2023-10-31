package controllers.validators

import controllers.errors.InvalidBody
import controllers.requests.ShowRequest
import play.api.libs.json.JsValue
import controllers.readers.ShowReader.readsShow
object ShowValidator {

  def validate(jsonBody: Option[JsValue]): Either[Throwable, Unit] = {
    jsonBody match {
      case None => Left(InvalidBody("body with invalid format for a JSON"))
      case Some(json) =>
        val showOption = json.asOpt[ShowRequest]
        showOption match {
          case None => Left(InvalidBody("invalid format in the json"))
          case Some(show) =>
            //Si llegue hasta aca, el cuerpo era valido!
            Right(())
        }

    }

  }
}