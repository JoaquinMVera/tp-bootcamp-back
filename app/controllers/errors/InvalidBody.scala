package controllers.errors

case class InvalidBody(message: String) extends Throwable(message)
