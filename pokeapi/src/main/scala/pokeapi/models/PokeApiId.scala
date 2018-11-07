package pokeapi.models

import play.api.libs.json._

case class PokeApiId (value: Int) extends AnyVal {
  override def toString: String = value.toString
}

object PokeApiId {
  implicit val reads: Reads[PokeApiId] = Reads(_.validate[Int].map(PokeApiId(_)))
  implicit val writes: Writes[PokeApiId] = Writes[PokeApiId](id => JsNumber(id.value))
}