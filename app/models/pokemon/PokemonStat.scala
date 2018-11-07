package models.pokemon

import play.api.libs.json.{Json, OFormat}
import pokeapi.models.pokemon.{PokemonStat => PokeApiStat}

case class PokemonStat
(
  name: String,
  baseStat: Int
)

object PokemonStat {
  implicit val format: OFormat[PokemonStat] = Json.format[PokemonStat]

  def fromPokeApiStats(ps: PokeApiStat): PokemonStat =
    PokemonStat(ps.name, ps.baseStat)
}