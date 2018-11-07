package pokeapi.client

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import play.api.cache.CacheApi
import play.api.libs.json.Reads
import play.api.libs.ws.WSClient
import pokeapi.exceptions.{NotFoundException, UndeserializableEntityException}
import pokeapi.models.ResponseList
import pokeapi.models.parameters.{Limit, Offset}
import pokeapi.models.pokemon.Pokemon
import pokeapi.models.types.Type

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

sealed trait Resource {
  protected val url: String
}

sealed trait Response[+T] {
  val isError: Boolean
  val body: T
}

case class Success[T](body: T) extends Response[T] { val isError = false }
case class Error(override val body: Nothing = throw new Exception()) extends Response[Nothing] { val isError = true }

@Singleton
class PokeApiClient @Inject()(
                              ws: WSClient,
                              configuration: Configuration,
                              cache: CacheApi
                              )
                             (implicit ec: ExecutionContext)
{
  protected val baseUrl: String = configuration.getString("baseUrl").getOrElse(throw new Exception("baseUrl required"))

  object Pokemon extends Resource {
    def list(): Future[Response[ResponseList]] =
      call[ResponseList](s"$url")

    def list(offset: Offset = Offset(Some(0)), limit: Limit = Limit(Some(20))): Future[Response[ResponseList]] =
      call[ResponseList](s"$url?$offset&$limit")

    def get(name: String): Future[Response[Pokemon]] = call[Pokemon](s"$url$name")

    def get(id: Int): Future[Response[Pokemon]] = call[Pokemon](s"$url$id")

    override protected val url: String = "pokemon/"
  }

  object Type extends Resource {
    def list(): Future[Response[ResponseList]] =
      call[ResponseList](s"$url")

    def list(offset: Offset = Offset(Some(0)), limit: Limit = Limit(Some(20))): Future[Response[ResponseList]] =
      call[ResponseList](s"$url?$offset&$limit")

    def get(name: String): Future[Response[Type]] = call[Type](s"$url$name")

    override protected val url: String = "type/"
  }

  private def call[T](url: String)(implicit reads: Reads[T]): Future[Response[T]] =
    cache.getOrElse(baseUrl + url, 5 minutes){
      ws.url(baseUrl + url).get().map{response =>
        if (response.status >= 200 && response.status < 300) {
          response.json.validate[T].fold({
            errors =>
              Error(throw UndeserializableEntityException(s"can't deserialize entity ${errors.mkString(" ")}"))
          }, Success(_))
        } else if(response.status == 404) {
          Error(throw NotFoundException(s"no pokemon found at this location ${baseUrl + url}"))
        } else {
          Error(throw new Exception(s"something went wrong /shrug"))
        }
      }
    }

}
