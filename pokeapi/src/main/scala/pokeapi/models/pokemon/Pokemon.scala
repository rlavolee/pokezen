package pokeapi.models.pokemon

import play.api.libs.json._
import pokeapi.models.PokeApiId

case class Pokemon
(
  id: PokeApiId,
  name: String,
  weight: Int,
  height: Int,
  sprites: PokemonSprites,
  types: List[PokemonType],
  stats: List[PokemonStat]
)

object Pokemon {
  implicit val reads: Reads[Pokemon] = Json.reads[Pokemon]
}