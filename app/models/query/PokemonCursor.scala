package models.query

import play.api.mvc.QueryStringBindable

case class PokemonCursor
(
  page: Int = 0,
  limit: Int = 30
)

object PokemonCursor {
  implicit def queryStringBindable(implicit intBinder: QueryStringBindable[Int]) = {
    new QueryStringBindable[PokemonCursor] {
      override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, PokemonCursor]] = {
        for {
          page <- intBinder.bind("page", params).orElse(Some(Right(0)))
          limit <- intBinder.bind("limit", params).orElse(Some(Right(30)))
        } yield {
          for {
            p <- page.right
            l <- limit.right
          } yield PokemonCursor(p, l)
        }
      }

      override def unbind(key: String, mc: PokemonCursor): String = {
        intBinder.unbind("page", mc.page) + "&" +
          intBinder.unbind("limit", mc.limit)
      }
    }
  }
}