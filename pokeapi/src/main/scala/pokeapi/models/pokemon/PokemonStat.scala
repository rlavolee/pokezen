package pokeapi.models.pokemon

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class PokemonStat
(
  name: String,
  baseStat: Int
)

object PokemonStat {

  implicit val reads: Reads[PokemonStat] = (
    (JsPath \ "stat" \ "name").read[String] and
    (JsPath \ "base_stat").read[Int]
  )(PokemonStat.apply _)

}