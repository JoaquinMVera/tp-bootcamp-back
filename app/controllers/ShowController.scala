package controllers

import accesData.entities.{Performance, Show}
import controllers.validators.{ShowValidator, VenueValidator}
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json.Json
import play.api.libs.json.OFormat.oFormatFromReadsAndOWrites
import play.api.mvc._
import writers.ShowWriter.writes
import writers.PerformanceWriter.writesPerformance

import java.time.{LocalDate, LocalDateTime}
import javax.inject.{Inject, Singleton}


@Singleton
class ShowController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {

    def addShow: Action[AnyContent] = Action {
      request =>
        val jsonOption = request.body.asJson

        val validation = ShowValidator.validate(jsonOption)

        validation match {
          case Left(error) => BadRequest(error.getMessage)
          case Right(()) =>
            //Que la magia aca devuelva el objeto entero creado
            val magiaDeServicio: Either[Throwable, Unit] = Right(())
            magiaDeServicio match {
              //Bad request para los casos feos que la capa de servicios (400) voy a tener que hacer un if aca
              case Left(error) => InternalServerError(error.getMessage)
              case Right(()) =>
                Ok("The show was added succesfully")
            }
        }

    }

  def getShows:Action[AnyContent] = Action {
    request =>
      val magiaDeServicio: Either[Throwable,Seq[Show]] = Right(Seq(Show(10,"Tan bionica","Cocinerto",10)))
      magiaDeServicio match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(list) =>
          Ok(Json.toJson(list))
      }
  }

  def getShow(id: Long): Action[AnyContent] = Action {
    request =>
      val magiaDeServicio: Either[Throwable,Seq[Performance]] = Right(Seq(Performance(10,LocalDate.now,true,14)))
      magiaDeServicio match {
        //Aca hay que tener en cuenta el 404, not found!
        case Left(error) => InternalServerError(error.getMessage)
        case Right(list) =>
          Ok(Json.toJson(list))
      }
  }

  def changeEstatePerformance(id:Long): Action[AnyContent] = Action {
        //Como el controller es a nivel show, hay que devolver el show
        //si el show no existe -> 404
        //si el performance no existe  o algo asi, es bad request
    request =>
      //Aca hay que atajar el body, y actualizar la performance. Una unica nos pasan por body, y el estado que me quieren desactivar
      val magiaDeServicio: Either[Throwable, Unit] = Right(())
      magiaDeServicio match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(()) =>
          Ok("The performance was put in pause/active")
      }
  }
}
