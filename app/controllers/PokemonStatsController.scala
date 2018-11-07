package controllers

import javax.inject.{Inject, Singleton}

import models.PokezenId
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}
import repository.PokemonAverageStatsRepository

import scala.concurrent.ExecutionContext

@Singleton
class PokemonStatsController @Inject()(
                                        pokemonAverageStatsRepository: PokemonAverageStatsRepository
                                      )(implicit ws: WSClient, ec: ExecutionContext)
extends Controller {

  def get(name: String) = Action.async {
    pokemonAverageStatsRepository.findByPokemonName(name).map{ p =>
      Ok(Json.toJson(p))
    }
  }
//
//  def list() = Action.async {
//    PokemonType.findBy().map { lp =>
//      Ok(Json.toJson(lp))
//    }
//  }

}
