package controllers.validators

import controllers.errors.InvalidBody
import controllers.requests.BookingRequest
import play.api.libs.json.JsValue
import controllers.readers.BookingReader.readsUser

import scala.util.{Failure, Success, Try}

object BookingValidator {
  def validate(jsonBody: Option[JsValue]): Either[Throwable, Unit] = {
    jsonBody match {
      case None => Left(InvalidBody("body with invalid format for a JSON"))
      case Some(json) =>
        val bookingOption = json.asOpt[BookingRequest]
        bookingOption match {
          case None => Left(InvalidBody("invalid format in the json"))
          case Some(booking) =>
            //Si llegue hasta aca, el cuerpo era valido!
            Right(())
        }
    }
  }
}
