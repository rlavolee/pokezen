package pokeapi.models

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.{JsNumber, Json}

class PokeApIdSpec extends Specification with Mockito {

  val id = 1234
  val pokeApiId = PokeApiId(id)

  "PokeApiId Value Class" should {
    "be created with this json" in {
      val r = Json.parse(Json.toJson(id).toString()).as[PokeApiId]

      r mustEqual pokeApiId
    }

    "be write as json value" in {
      val r = Json.toJson(pokeApiId)

      r mustEqual JsNumber(id)
    }
  }

}
