package controllers

import javax.inject.{Inject, Singleton}

import models.pokemon.like.PokemonLike
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import repository.PokemonLikeRepository

import scala.concurrent.ExecutionContext

@Singleton
class PokemonLikeController @Inject()(
                                      pokemonLikeRepository: PokemonLikeRepository
                                     )
                                     (implicit ec: ExecutionContext)
extends Controller {

  private val logger: Logger = Logger(this.getClass)

  def get(name: String) = Action.async {
    pokemonLikeRepository.findOneByName(name)
      .map(p => Ok(Json.toJson(p)))
      .recover{ case t =>
        logger.error(t.getMessage)
        NotFound(s"No Pokémon found with name $name")
      }
  }

  def like(name: String) = Action.async { implicit request =>
    pokemonLikeRepository.like(name)
        .map(_ => NoContent)
        .recover{case t =>
          logger.error(t.getMessage)
          UnprocessableEntity
        }
  }

  def dislike(name: String) = Action.async { implicit request =>
    pokemonLikeRepository.dislike(name)
      .map(_ => NoContent)
      .recover{case t =>
        logger.error(t.getMessage)
        UnprocessableEntity
      }
  }

}
