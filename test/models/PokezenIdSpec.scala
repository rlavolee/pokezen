package models

import java.util.UUID

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.{JsString, Json}

class PokezenIdSpec extends Specification with Mockito {

  val uuid = "702ee4ae-65ef-4402-a525-a2c44a78cbe9"
  val pokemonId = PokezenId(UUID.fromString(uuid))

  "PokemonId Value Class" should {
    "be created with this json" in {
      val r = Json.parse(Json.toJson(uuid).toString()).as[PokezenId]

      r mustEqual pokemonId
    }

    "be write as json value" in {
      val r = Json.toJson(pokemonId)

      r mustEqual JsString(uuid)
    }

    "be created from a UUID string" in {
      val r = PokezenId.fromString(uuid).get

      r mustEqual pokemonId
    }
  }

}
