package repository

import models.pokemon.like.PokemonLike

import scala.concurrent.Future

trait PokemonLikeRepository {
  protected val repositoryName: String = "pokemonLike"

  def findOneByName(name: String): Future[PokemonLike]

  def save(pokemon: PokemonLike): Future[PokemonLike]

  def like(name: String): Future[PokemonLike]

  def dislike(name: String): Future[PokemonLike]
}
