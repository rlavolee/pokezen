package pokeapi.models.pokemon

import play.api.libs.json.{Json, Reads}

case class PokemonSprites
(
  back_female: Option[String],
  back_shiny_female: Option[String],
  back_default: Option[String],
  front_female: Option[String],
  front_shiny_female: Option[String],
  back_shiny: Option[String],
  front_default: Option[String],
  front_shiny: Option[String]
)

object PokemonSprites {
  implicit val reads: Reads[PokemonSprites] = Json.reads[PokemonSprites]
}