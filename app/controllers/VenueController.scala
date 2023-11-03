package controllers
import accesData.entities.{Venue}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import javax.inject._
import play.api.mvc._
import requests.VenueRequest
import accesData.entities.VenueWriter.writesVenue
import requests.VenueRequest.readsVenue
import services.VenueService
import syntax.errors.RequestError



@Singleton
class VenueController @Inject()(val cc: ControllerComponents,venueService: VenueService) extends AbstractController(cc) {

  //En el service falta atrapar negativo

  //aca tengo que hacer inyeccion de dependencia
  def addVenue: Action[JsValue] = Action(parse.json) {
    request =>
      //Me gustaria chequear que me pasen un json, primeramente

      //esto es hacer la validacion de si esta bien el formato, sin tener que hacer un validator custom
      val maybeVenueRequest = request.body.validate[VenueRequest]

      maybeVenueRequest match {
        case JsError(errors) => BadRequest("Invalid json format")
        case JsSuccess(venueRequest, path) =>

          val maybeVenue = venueService.addVenue(venueRequest)

          maybeVenue match {
            case Left(error:RequestError) => BadRequest(error.getMessage)
            case Left(error) => InternalServerError(error.getMessage)
            case Right(venue) => Ok(Json.toJson(venue)) // Ok(Venue.asJson)
          }

      }

  }

  def getVenues: Action[AnyContent] = Action {
      val maybeVenueSeq: Either[Throwable, Seq[Venue]] = venueService.getAllVenues
    maybeVenueSeq match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(venueSeq) =>
          Ok(Json.toJson(venueSeq))
      }
  }


}

