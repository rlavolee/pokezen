package pokeapi.models.types

import play.api.libs.json.{Json, Reads}
import pokeapi.models.Result

case class TypePokemon (pokemon: Result)

object TypePokemon {
  implicit val reads: Reads[TypePokemon] = Json.reads[TypePokemon]
}