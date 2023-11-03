package controllers


import controllers.requests.{PerformanceStateRequest, ShowRequest}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc._
import services.{ShowService}
import syntax.errors.{NotFoundError, RequestError}
import accesData.entities.ShowWriter._
import accesData.entities.PerformanceWriter.writesPerformance
import views.ShowViewer.writesShowViewer
import javax.inject.{Inject, Singleton}


@Singleton
class ShowController @Inject()(val cc: ControllerComponents,showService: ShowService) extends AbstractController(cc) {

  def addShow: Action[JsValue] = Action(parse.json) {
    request =>
      val maybeShowRequest = request.body.validate[ShowRequest]

      maybeShowRequest match {
        case JsError(errors) => BadRequest("Invalid json format")
        case JsSuccess(showRequest, path) =>
          val maybeShow = showService.add(showRequest)
          maybeShow match {
            case Left(error:RequestError) => BadRequest(error.getMessage)
            case Left(error) => InternalServerError(error.getMessage)
            case Right(show) =>
              Ok(Json.toJson(show))
          }

      }
    //Que la magia aca devuelva el objeto entero creado
  }


  def getShows: Action[AnyContent] = Action {
      val maybeShowsSeq = showService.getAllShows
    maybeShowsSeq match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(showsSeq) =>
          Ok(Json.toJson(showsSeq))
      }
  }

  def getShow(id: Long): Action[AnyContent] = Action {
      val maybeShowViewer = showService.getShowViewerById(id)
      maybeShowViewer match {
        //Aca hay que tener en cuenta el 404, not found!
        case Left(error: NotFoundError) => NotFound(error.getMessage)
        case Left(error) => InternalServerError(error.getMessage)
        case Right(showViewer) =>
          Ok(Json.toJson(showViewer))
      }
  }

  def changeEstatePerformance(id: Long): Action[JsValue] = Action(parse.json) {
    //Como el controller es a nivel show, hay que devolver el show
    //si el show no existe -> 404
    //si el performance no existe  o algo asi, es bad request
    request =>

      val maybePerformanceStateRequest = request.body.validate[PerformanceStateRequest]

      maybePerformanceStateRequest match {
        case JsError(errors) => BadRequest("Invalid json format")
        case JsSuccess(performanceStateRequest, path) =>
          //Aca hay que atajar el body, y actualizar la performance. Una unica nos pasan por body, y el estado que me quieren desactivar
          val maybePerformance = showService.changePerformanceState(id,performanceStateRequest)
          maybePerformance match {
            case Left(error: NotFoundError) => NotFound(error.getMessage)
            case Left(error:RequestError) => BadRequest(error.getMessage)
            case Left(error) => InternalServerError(error.getMessage)
            case Right(performance) =>
              Ok(Json.toJson(performance))
          }
      }
  }

}
