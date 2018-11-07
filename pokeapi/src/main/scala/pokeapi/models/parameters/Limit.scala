package pokeapi.models.parameters

case class Limit(value: Option[Int]) extends AnyVal {
  override def toString: String = {
    value match {
      case Some(i) => s"limit=$i"
      case None => ""
    }
  }
}