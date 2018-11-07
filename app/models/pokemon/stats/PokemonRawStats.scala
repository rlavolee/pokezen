package models.pokemon.stats

import models.pokemon.PokemonStat
import play.api.libs.json.{Json, OFormat}

case class PokemonRawStats
(
  `type`: String,
  numberOfPokemon: Int = 0,
  rawStats: List[PokemonStat] = List.empty
)

object PokemonRawStats {
  implicit val format: OFormat[PokemonRawStats] = Json.format[PokemonRawStats]
}