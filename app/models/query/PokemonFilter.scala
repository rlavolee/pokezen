package models.query

import play.api.libs.json.Json
import play.api.mvc.QueryStringBindable

case class PokemonFilter
(
  name: String = ""
)

object PokemonFilter {

  implicit val format = Json.format[PokemonFilter]

  implicit def queryStringBindable
  (implicit binder: QueryStringBindable[String]) = new QueryStringBindable[PokemonFilter] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, PokemonFilter]] = {
      params.get("name") match {
        case Some(n) => Some(Right(PokemonFilter(n.mkString(""))))
        case _ => None
      }
    }

    override def unbind(key: String, pf: PokemonFilter): String = s"name=${pf.name}"
  }
}