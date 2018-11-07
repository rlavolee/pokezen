package pokeapi.models

import play.api.libs.json.{Json, Reads}

case class ResponseList
(
  count: Int,
  previous: Option[String],
  results: List[Result],
  next: Option[String]
)

object ResponseList {
  implicit val reads: Reads[ResponseList] = Json.reads[ResponseList]
}