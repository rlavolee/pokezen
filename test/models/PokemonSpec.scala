package models

import models.pokemon.{Pokemon, PokemonSprites, PokemonStat}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.libs.json.Json

class PokemonSpec extends Specification with Mockito {

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
  val json: String =
    """{
      |"name":"pikachu",
      |"weight":10,
      |"height":10,
      |"sprites": {
      | "backFemale":"http://somehost.com/plop.jpg",
      | "backShinyFemale":"http://somehost.com/plop.jpg",
      | "backDefault":"http://somehost.com/plop.jpg",
      | "frontFemale":"http://somehost.com/plop.jpg",
      | "frontShinyFemale":"http://somehost.com/plop.jpg",
      | "backShiny":"http://somehost.com/plop.jpg",
      | "frontDefault":"http://somehost.com/plop.jpg",
      | "frontShiny":"http://somehost.com/plop.jpg"
      |},
      |"types":["electric"],
      |"stats":[{"name":"speed","baseStat":5},{"name":"jump","baseStat":2}]
      |}""".stripMargin

  "Pokemon Model" should {
    "be created with this json" in {
      val r = Json.parse(json).as[Pokemon]

      r mustEqual pikachu
    }

    "be write as json value" in {
      val r = Json.toJson(pikachu)

      r mustEqual Json.parse(json)
    }
  }

}
