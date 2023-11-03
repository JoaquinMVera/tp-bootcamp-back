package services

import accesData.entities.User
import accesData.repositories.{UserRepository}
import controllers.requests.UserRequest
import syntax.errors.{RequestError}
import javax.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.Try
import accesData.DatabaseConfigModule
import slick.dbio.DBIO
import scala.concurrent.ExecutionContext.Implicits.global



class UserService @Inject()(userRepository: UserRepository) extends DatabaseConfigModule {


  //dar de alta un usuario nuevo, listar usuarios, buscar un user por el id, y acredar plata en la cuenta de un user por id

  def addUser(userRequest: UserRequest): Either[Throwable, User] = {

    validateUserRequest(userRequest).flatMap { _ =>
      val dbioUser = for {
        user <- userRepository.add(userRequest)
      } yield user

      Try(Await.result(db.run(dbioUser), 10.seconds)).toEither
    }
  }

  def getAllUsers: Either[Throwable, Seq[User]] = {
    Try(Await.result(db.run(userRepository.findAll), 10.seconds)).toEither
  }

  def getUserById(id: Long): Either[Throwable, User] = {
    Try(Await.result(db.run(userRepository.findById(id)), 10.seconds)).toEither
  }

  def addMoney(id: Long, amount: BigDecimal): Either[Throwable, User] = {

    val dbioMoney = for {
      user <- userRepository.findById(id)
      _ <- validateAmount(amount)
      _ <- userRepository.updateBalance(id, user.balance + amount)
      user <- userRepository.findById(id)
    } yield user

    Try(Await.result(db.run(dbioMoney), 10.seconds)).toEither

  }


  private def validateUserRequest(userRequest: UserRequest): Either[Throwable, Unit] = {
    userRequest match {
      case _ if userRequest.name.isEmpty => Left(RequestError("The user name is empty"))
      case _ if userRequest.email.isEmpty => Left(RequestError("The user email is empty"))
      case _ => Right(())
    }
  }

  private def validateAmount(amount: BigDecimal) = {
    amount match {
      case _ if amount <= 0 => DBIO.failed(RequestError("The amount must be more than 0"))
      case _ => DBIO.successful(())
    }
  }


}