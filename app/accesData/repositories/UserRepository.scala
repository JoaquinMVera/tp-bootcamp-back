package accesData.repositories

import accesData.database.{UsersTable}
import accesData.entities.{User}
import controllers.requests.UserRequest
import slick.dbio.{DBIO, _}
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import syntax.errors.NotFoundError
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepository {

  private val usersTable = TableQuery[UsersTable]


  def add(userRequest: UserRequest)  = {
    val user = User(Int.MinValue,userRequest.name,userRequest.email,0)
    val insertVenueQuery   = (usersTable returning(usersTable)) += user
    insertVenueQuery
  }

  def findAll  = {
    val findAllQuery = usersTable.result
   findAllQuery
  }

  def findById(userId: Long) = {

    val findByIdQuery = usersTable.filter(_.id === userId).forUpdate.result.headOption.flatMap{
      case Some(user) => DBIO.successful(user)
      case None => DBIO.failed(NotFoundError(s"Not found an user with $userId"))
    }
    findByIdQuery
  }

  def updateBalance(userId:Long, amount: BigDecimal) = {
    val updateBalanceQuery = usersTable.filter(_.id === userId).map(_.balance).update(amount)
    updateBalanceQuery
  }

  //val join = Await.result(db.run(joinQuery.result), 10.seconds)
}
