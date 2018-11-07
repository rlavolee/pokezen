package models.pokemon

import play.api.libs.json.{Json, OFormat}
import pokeapi.models.pokemon.{PokemonSprites => PokeApiSprites}

case class PokemonSprites
(
  backFemale: Option[String],
  backShinyFemale: Option[String],
  backDefault: Option[String],
  frontFemale: Option[String],
  frontShinyFemale: Option[String],
  backShiny: Option[String],
  frontDefault: Option[String],
  frontShiny: Option[String]
)

object PokemonSprites {
  implicit val format: OFormat[PokemonSprites] = Json.format[PokemonSprites]

  def fromPokeApiSprite(ps: PokeApiSprites): PokemonSprites =
    PokemonSprites(
      ps.back_female,
      ps.back_shiny_female,
      ps.back_default,
      ps.front_female,
      ps.front_shiny_female,
      ps.back_shiny,
      ps.front_default,
      ps.front_shiny
    )
}