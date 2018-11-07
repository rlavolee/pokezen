package repository

import models.pokemon.stats.{PokemonAverageStats, PokemonRawStats}

import scala.concurrent.Future

trait PokemonAverageStatsRepository {
  protected val repositoryName: String = "pokemonAverageStats"

  def findByPokemonName(name: String): Future[List[PokemonAverageStats]]
}
