package syntax.errors

case class NotFoundError(message:String) extends Throwable(message)
