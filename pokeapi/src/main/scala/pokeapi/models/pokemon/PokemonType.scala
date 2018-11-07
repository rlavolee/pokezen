package pokeapi.models.pokemon

import play.api.libs.json._

case class PokemonType
(
  name: String
)

object PokemonType {
  implicit val reads: Reads[PokemonType] = (JsPath \ "type" \ "name").read(Reads.of[String]).map(PokemonType(_))
}