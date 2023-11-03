package syntax.errors

case class DepositRequestError(message: String) extends Throwable(message)
