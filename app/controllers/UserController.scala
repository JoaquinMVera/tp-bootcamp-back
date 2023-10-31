package controllers

import accesData.entities.{Show, User}
import controllers.validators.{DepositValidator, UserValidator}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import controllers.writers.UserWriter.writesUser

import javax.inject.{Inject, Singleton}

@Singleton
class UserController @Inject()(val cc: ControllerComponents) extends AbstractController(cc) {

  def addUser: Action[AnyContent] = Action {
    request =>
      val jsonOption = request.body.asJson

      val validation = UserValidator.validate(jsonOption)

      validation match {
        case Left(error) => BadRequest(error.getMessage)
        case Right(()) =>
          val magiaDeServicio: Either[Throwable, Unit] = Right(())
          magiaDeServicio match {
            //falta otro caso de badRequest!
            case Left(error) => InternalServerError(error.getMessage)
            case Right(()) =>
              Ok("The user was added succesfully")
          }
      }
  }

  def getUsers : Action[AnyContent] = Action {
    request =>
      val magiaDeServicio: Either[Throwable, Seq[User]] = Right(Seq(User(10,"Joaquiño","joaco@gmail.com",100)))
      magiaDeServicio match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(list) =>
          Ok(Json.toJson(list))
      }
  }

  def getUser(id: Long): Action[AnyContent] = Action {
    request =>
      val magiaDeServicio: Either[Throwable, User] = Right((User(id,"joaco","emailexample",1000)))
      //Aca iria un bool? o incluso otro either, para ver si lo encontre o no. no creo que este bien un internal server error
      //si no lo encuentro no?
        magiaDeServicio match {
        case Left(error) => InternalServerError(error.getMessage)
        case Right(user) =>
          Ok(Json.toJson(user))
      }
  }


  def depositMoney(id: Long): Action[AnyContent] = Action {
    request =>
        val jsonOption = request.body.asJson

        val validation = DepositValidator.validate(jsonOption)

        validation match {
          case Left(error) => BadRequest(error.getMessage)
          case Right(()) =>
            val magiaDeServicio: Either[Throwable, Unit] = Right(())
            magiaDeServicio match {
              case Left(error) => Conflict(error.getMessage)
              case Right(()) =>
                Ok("The deposit was succesfull,new balance is TODO")
            }
        }
  }

}
