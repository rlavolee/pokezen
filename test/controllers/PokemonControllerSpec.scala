package controllers

import models.pokemon.{Pokemon, PokemonSprites, PokemonStat}
import org.specs2.mock.Mockito
import play.api.libs.json.Json
import play.api.test.{FakeRequest, PlaySpecification}
import repository.PokemonRepository

import scala.concurrent.{ExecutionContext, Future}

class PokemonControllerSpec extends PlaySpecification with Mockito {

  "PokemonController" should {

    "returns Pokemon when requesting by name" in { implicit ec: ExecutionContext =>

      val sprites = PokemonSprites(
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg"),
        Some("http://somehost.com/plop.jpg")
      )

      val stats = List(PokemonStat("speed", 5), PokemonStat("jump", 2))
      val pikachu = Pokemon("pikachu", 10, 10, sprites, List("electric"), stats)

      val mockPokemonRepository = mock[PokemonRepository]
      mockPokemonRepository.findOneByName(anyString) returns Future(pikachu)

      val sut = new PokemonController(mockPokemonRepository)
      val result = sut.getByName(anyString)(FakeRequest())
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      contentAsJson(result) must beEqualTo(Json.toJson(pikachu))
    }

  }

}
