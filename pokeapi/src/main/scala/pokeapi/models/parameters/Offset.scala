package pokeapi.models.parameters

case class Offset (value: Option[Int]) extends AnyVal {
  override def toString: String = {
    value match {
      case Some(i) => s"offset=$i"
      case None => ""
    }
  }
}