package pokeapi.client

import org.specs2.concurrent.ExecutionEnv
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.Configuration
import play.api.cache.CacheApi
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import pokeapi.exceptions.NotFoundException
import pokeapi.models.pokemon.Pokemon

import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

class PokeApClientSpec extends Specification with Mockito {

  "PokeApClient" should {

    "throws an exception if baseUrl value is undefined in conf file" in { implicit ec: ExecutionContext =>
      val mockWS: WSClient = mock[WSClient]
      val mockCache: CacheApi = mock[CacheApi]
      val mockPokemon: Pokemon = mock[Pokemon]
      val mockConfiguration: Configuration = mock[Configuration]

      mockConfiguration.getString("baseUrl") returns None

      new PokeApiClient(mockWS, mockConfiguration, mockCache) must throwAn[Exception]("baseUrl required")
    }

    "returns pokemon by name" in { implicit ee: ExecutionEnv =>
      val mockWS: WSClient = mock[WSClient]
      val mockCache: CacheApi = mock[CacheApi]
      val mockPokemon: Pokemon = mock[Pokemon]
      val mockConfiguration: Configuration = mock[Configuration]

      mockConfiguration.getString("baseUrl") returns Some("http://somehost.com/")

      mockPokemon.name returns "butterfree"

      mockCache.getOrElse[Future[Response[Pokemon]]](anyString, any[Duration])(any)(any) returns Future(Success(mockPokemon))

      val sut = new PokeApiClient(mockWS, mockConfiguration, mockCache)(ee.executionContext)
      sut.Pokemon.get("butterfree").map(_.body.name) must beEqualTo("butterfree").await
    }

    "throws NotFoundException when response code is 404" in { implicit ee: ExecutionEnv =>
      val mockWS: WSClient = mock[WSClient]
      val mockCache: CacheApi = mock[CacheApi]
      val mockPokemon: Pokemon = mock[Pokemon]
      val mockConfiguration: Configuration = mock[Configuration]
      val mockWSRequest: WSRequest = mock[WSRequest]
      val mockWSResponse: WSResponse = mock[WSResponse]

      mockWSResponse.status returns 404
      mockWSRequest.get() returns Future(mockWSResponse)

      mockWS.url("http://somehost.com/pokemon/butterfree") returns mockWSRequest

      mockConfiguration.getString("baseUrl") returns Some("http://somehost.com/")

      mockCache.getOrElse[Future[Response[Pokemon]]](anyString, any[Duration])(any)(any) returns Future(Error(throw NotFoundException("")))

      val sut = new PokeApiClient(mockWS, mockConfiguration, mockCache)(ee.executionContext)
      sut.Pokemon.get("butterfree") must throwAn[NotFoundException]("").await
    }
  }

}
