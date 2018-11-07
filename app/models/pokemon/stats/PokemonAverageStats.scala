package models.pokemon.stats

import models.pokemon.{Pokemon, PokemonStat}
import play.api.libs.json.{Json, OFormat}

case class PokemonAverageStats
(
  name: String,
  `type`: String,
  average: List[PokemonStat] = List.empty
)

object PokemonAverageStats {
  implicit val format: OFormat[PokemonAverageStats] = Json.format[PokemonAverageStats]

  def apply(pokemon: Pokemon, pokemonAverageStats: PokemonRawStats): Option[PokemonAverageStats] = {
    if(pokemon.types.contains(pokemonAverageStats.`type`))
    {
      val stats = pokemonAverageStats.rawStats.map{ stat =>
        PokemonStat(stat.name, stat.baseStat / pokemonAverageStats.numberOfPokemon)
      }
      Some(PokemonAverageStats(pokemon.name, pokemonAverageStats.`type`, stats))
    }
    else None
  }
}