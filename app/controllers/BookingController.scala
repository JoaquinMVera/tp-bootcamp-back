package controllers

import accesData.entities.{Booking, Tickets}
import controllers.validators.{BookingValidator, UserValidator}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import controllers.writers.BookingWriter.writesPerformance

import java.time.LocalDate
import javax.inject.{Inject, Singleton}


@Singleton
class BookingController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {

  def buyTickets: Action[AnyContent] = Action {
    request =>
      val jsonOption = request.body.asJson

      val validation = BookingValidator.validate(jsonOption)

      validation match {
        case Left(error) => BadRequest(error.getMessage)
        case Right(()) =>
          val magiaDeServicio: Either[Throwable, Unit] = Right(())
          magiaDeServicio match {
            case Left(error) => Conflict(error.getMessage)
            case Right(()) =>
              Ok("The buy was succesfull")
          }
      }
  }

  def getTickets(user: Long): Action[AnyContent] = Action{
    request =>
      val magiaDeServicio: Either[Throwable, Seq[Booking]] = Right(Seq(Booking(100,10,LocalDate.now,10,2,user)))
      magiaDeServicio match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(list) =>
          Ok(Json.toJson(list))
      }
  }


}

