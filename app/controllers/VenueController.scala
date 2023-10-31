package controllers
import accesData.entities.{Show, Venue}
import play.api.libs.json.Json

import javax.inject._
import play.api.mvc._
import requests.VenueRequest
import validators.VenueValidator
import writers.VenueWriter.writesVenue



@Singleton
class VenueController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {

  //En el service falta atrapar negativo

  def addVenue: Action[AnyContent] = Action {
    request =>
      //Me gustaria chequear que me pasen un json, primeramente
      val jsonOption = request.body.asJson

        val validation = VenueValidator.validate(jsonOption)

        validation match {
          case Left(error) => BadRequest(error.getMessage)
          case Right(()) =>
            val magiaDeServicio: Either[Throwable, Unit] = Right(())
            magiaDeServicio match {
              case Left(error) => InternalServerError(error.getMessage)
              case Right(()) => Ok("The venue was added succesfully")
            }
        }
  }

  def getVenues: Action[AnyContent] = Action {
    request =>
      val magiaDeServicio: Either[Throwable, Seq[Venue]] = Right(Seq(Venue(10,"Luna Park","capital",100)))
      magiaDeServicio match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(list) =>
          Ok(Json.toJson(list))
      }
  }


}

