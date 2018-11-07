package helpers

import scala.concurrent.Future

object ExtendedOption {

  implicit class ExtendedOption[T](val option: Option[T]) {
    def toFuture(throwable: Throwable): Future[T] = option match {
      case Some(value) => Future.successful(value)
      case None => Future.failed(throwable)
    }
  }

}
