package controllers.validators

import controllers.errors.InvalidBody
import controllers.requests.UserRequest
import play.api.libs.json.JsValue
import controllers.readers.UserReader.readsUser

object UserValidator {
  def validate(jsonBody: Option[JsValue]): Either[Throwable, Unit] = {
    jsonBody match {
      case None => Left(InvalidBody("body with invalid format for a JSON"))
      case Some(json) =>
        val userOption = json.asOpt[UserRequest]
        userOption match {
          case None => Left(InvalidBody("invalid format in the json"))
          case Some(user) =>
            //Si llegue hasta aca, el cuerpo era valido!
            Right(())
        }

    }

  }
}
