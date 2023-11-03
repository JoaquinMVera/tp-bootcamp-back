package controllers

import accesData.entities.{User}
import controllers.requests.{DepositRequest, UserRequest}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import accesData.entities.UserWriter.writesUser
import requests.UserRequest.readsUser
import services.UserService
import syntax.errors.{NotFoundError, RequestError}
import javax.inject.{Inject, Singleton}

@Singleton
class UserController @Inject()(val cc: ControllerComponents, userService: UserService) extends AbstractController(cc) {


  def addUser: Action[JsValue] = Action(parse.json) {
    request =>
      val maybeUserRequest = request.body.validate[UserRequest]

      maybeUserRequest match {
        case JsError(errors) => BadRequest("Invalid json format")
        case JsSuccess(userRequest, path) =>
          val maybeUser: Either[Throwable, User] = userService.addUser(userRequest)
          maybeUser match {
            //Esto de abajo es porque quiero dar un BadRequest cuando hagan algo invalido de logica (nombre empty, por ejemplo)
            case Left(error: RequestError) => BadRequest(error.getMessage)

            case Left(error) => InternalServerError(error.getMessage)
            case Right(user) => Ok(Json.toJson(user)) // Ok(Venue.asJson)
          }
      }
  }

  def getUsers: Action[AnyContent] = Action {

    val maybeUsers: Either[Throwable, Seq[User]] = userService.getAllUsers
    maybeUsers match {
      case Left(error) => InternalServerError(error.getMessage)
      case Right(userList) =>
        Ok(Json.toJson(userList))
    }
  }

  def getUser(id: Long): Action[AnyContent] = Action {

    val maybeUser: Either[Throwable, User] = userService.getUserById(id)
    //Aca iria un bool? o incluso otro either, para ver si lo encontre o no. no creo que este bien un internal server error
    //si no lo encuentro no?
    maybeUser match {

      //Esto de abajo es un error por si no encuentro el user! (si ese ID no existe)
      case Left(error: NotFoundError) => NotFound(error.getMessage)
      case Left(error) => InternalServerError(error.getMessage)
      case Right(user) =>
        Ok(Json.toJson(user))
    }
  }


  def depositMoney(id: Long): Action[JsValue] = Action(parse.json) {
    request =>
      val maybeDepositRequest = request.body.validate[DepositRequest]

      maybeDepositRequest match {
        case JsError(errors) => BadRequest("Invalid json format")
        case JsSuccess(depositRequest, path) =>

          val maybeUser: Either[Throwable, User] = userService.addMoney(id, depositRequest.amount)

          maybeUser match {
            //Lo mismo que antes, si no encuentro el id, NotFound
            case Left(error: NotFoundError) => NotFound(error.getMessage)
            //Si me pasaron un amount negativo, no me gusta nada
            case Left(error: RequestError) => BadRequest(error.getMessage)

            case Left(error) => InternalServerError(error.getMessage)
            case Right(user) => Ok(Json.toJson(user)) // Ok(Venue.asJson)
          }
      }
  }

}
