package syntax.errors

case class RequestError(message: String) extends Throwable(message)
