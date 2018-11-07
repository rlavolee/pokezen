package repository

import models.pokemon.Pokemon

import scala.concurrent.Future

trait PokemonRepository {
  protected val repositoryName: String = "pokemon"

  def searchByName(name: String): Future[Set[String]]

  def findByName(name: String): Future[Set[Pokemon]]

  def findOneByName(name: String): Future[Pokemon]
}