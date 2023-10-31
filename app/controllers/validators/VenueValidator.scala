package controllers.validators


import controllers.errors.InvalidBody
import controllers.requests.VenueRequest
import play.api.libs.json.JsValue
import controllers.readers.VenueReader.readsVenue
object VenueValidator {

  def validate(jsonBody : Option[JsValue]): Either[Throwable,Unit] = {
      jsonBody match {
        case None => Left(InvalidBody("body with invalid format for a JSON"))
        case Some(json) =>
          val venueOption = json.asOpt[VenueRequest]
          venueOption match {
            case None => Left(InvalidBody("invalid format in the json"))
            case Some(venue) =>
              //Si llegue hasta aca, el cuerpo era valido!
              Right(())
          }

      }
  }

}
