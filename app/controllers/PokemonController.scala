package controllers

import javax.inject.{Inject, Singleton}

import exceptions.NoPokemonFoundException
import models.query.PokemonFilter
import play.api.Logger
import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import pokeapi.exceptions.NotFoundException
import repository.PokemonRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PokemonController @Inject()(
                                   pokemonRepository: PokemonRepository
                                 )(implicit ec: ExecutionContext)
  extends Controller {

  private val logger: Logger = Logger(this.getClass)

  def index() = Action.async {
    Future.successful(Ok(views.html.index()))
  }

  def getByName(name: String) = Action.async {
    pokemonRepository.findOneByName(name)
      .map(pokemon => Ok(Json.toJson(pokemon)))
      .recover{
        case _: NotFoundException => NotFound(s"No Pokémon found with name $name")
        case t =>
        logger.error(t.getMessage)
        InternalServerError
      }
  }

  def filter(filter: PokemonFilter) = Action.async {
    pokemonRepository.searchByName(filter.name)
      .map(pokemonNames => Ok(Json.toJson(pokemonNames)))
      .recover{
        case _: NoPokemonFoundException => NotFound(s"No Pokémon found with name ${filter.name}")
        case t =>
        logger.error(t.getMessage)
        InternalServerError
      }
  }
}
