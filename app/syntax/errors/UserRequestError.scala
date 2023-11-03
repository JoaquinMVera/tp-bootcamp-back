package syntax.errors

case class UserRequestError(message:String) extends Throwable(message)
