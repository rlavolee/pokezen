package pokeapi.models.types

import play.api.libs.json.{Json, Reads}
import pokeapi.models.PokeApiId

case class Type
(
  id: PokeApiId,
  name: String,
  pokemon: List[TypePokemon]
)

object Type {
  implicit val reads: Reads[Type] = Json.reads[Type]
}