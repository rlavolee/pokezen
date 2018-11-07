package models.pokemon

import play.api.libs.json.{Json, OFormat}
import pokeapi.models.pokemon.{Pokemon => PokemonApi}

case class Pokemon
(
  name: String,
  weight: Int,
  height: Int,
  sprites: PokemonSprites,
  types: List[String],
  stats: List[PokemonStat] = Nil
)

object Pokemon {

  implicit val format: OFormat[Pokemon] = Json.format[Pokemon]

  def fromPokeApi(p: PokemonApi): Pokemon = {
    Pokemon(
      p.name,
      p.weight,
      p.height,
      PokemonSprites.fromPokeApiSprite(p.sprites),
      p.types.map(_.name),
      p.stats.map(PokemonStat.fromPokeApiStats)
    )
  }
}