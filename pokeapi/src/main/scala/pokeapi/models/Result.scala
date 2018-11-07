package pokeapi.models

import play.api.libs.json.{Json, Reads}

case class Result
(
  url: String,
  name: String
) {
  val id: PokeApiId = PokeApiId(url.split("/").last.toInt)
}

object Result {
  implicit val reads: Reads[Result] = Json.reads[Result]
}
