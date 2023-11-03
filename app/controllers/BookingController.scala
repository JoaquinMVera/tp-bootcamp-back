package controllers

import controllers.requests.BookingRequest
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import accesData.entities.BookingWriter.writesPerformance
import services.BookingService
import controllers.requests.BookingRequest.readsUser
import syntax.errors.{NotFoundError, RequestError}
import javax.inject.{Inject, Singleton}


@Singleton
class BookingController @Inject()(val cc: ControllerComponents, bookingService: BookingService) extends AbstractController(cc) {

  def buyTickets: Action[JsValue] = Action(parse.json) {
    request =>
      val maybeBookingRequest = request.body.validate[BookingRequest]

      maybeBookingRequest match {
        case JsError(errors) => BadRequest("Invalid json format")
        case JsSuccess(bookingRequest, path) =>
          val maybeBooking = bookingService.buyTickets(bookingRequest)
          maybeBooking match {
            //Bad request para los casos feos que la capa de servicios (400) voy a tener que hacer un if aca
            case Left(error: RequestError) => BadRequest(error.getMessage)
            case Left(error) => InternalServerError(error.getMessage)
            case Right(booking) =>
              Ok(Json.toJson(booking))
          }

      }
  }

  def getBookings(user: Long): Action[AnyContent] = Action {
    val maybeBookingSeq = bookingService.getBookings(user)
    maybeBookingSeq match {
      case Left(error: NotFoundError) => NotFound(error.getMessage)
      case Left(error) => InternalServerError(error.getMessage)
      case Right(bookingsSeq) =>
        Ok(Json.toJson(bookingsSeq))
    }
  }


}

