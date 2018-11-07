package models.pokemon.like

import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.{Json, OFormat}

case class PokemonLike
(
  name: String,
  like: Int = 0
)

object PokemonLike {

  implicit val format: OFormat[PokemonLike] = Json.format[PokemonLike]

  val formMapping = Form(
    mapping(
      "name" -> text,
      "like" -> number
    )(PokemonLike.apply)(PokemonLike.unapply)
  )

}