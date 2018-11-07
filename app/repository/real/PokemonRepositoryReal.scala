package repository.real

import javax.inject.{Inject, Singleton}

import exceptions.NoPokemonFoundException
import models.pokemon.Pokemon
import modules.ElasticClient
import pokeapi.client.PokeApiClient
import repository.PokemonRepository

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PokemonRepositoryReal @Inject()(
                                       elastic: ElasticClient,
                                       pokeApi: PokeApiClient
                                     )(implicit ec: ExecutionContext)
  extends PokemonRepository {

  override def searchByName(name: String): Future[Set[String]] = {
    import com.sksamuel.elastic4s.http.ElasticDsl._
    for {
      response <-
      elastic.client.execute {
        searchWithType("pokezen" / repositoryName).prefix("name", name).limit(10)
      }
      _ <- {
        if (response.isError) Future.failed(NoPokemonFoundException(s"No Pokémon found with $name value"))
        else Future.successful(())
      }
      pokemonNames = response.result.hits.hits.flatMap(_.sourceFieldOpt("name").map(_.toString)).toSet
    }
    yield pokemonNames
  }

  override def findByName(name: String): Future[Set[Pokemon]] = {
    for {
      pokemonNames <- searchByName(name)
      pokemons <- Future.sequence(pokemonNames.map(findOneByName))
    } yield pokemons
  }

  override def findOneByName(name: String): Future[Pokemon] =
    for {
      response <- pokeApi.Pokemon.get(name)
    } yield Pokemon.fromPokeApi(response.body)
}
