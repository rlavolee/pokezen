package repository.real

import javax.inject.{Inject, Singleton}

import helpers.MongoIndex
import models.pokemon.stats.{PokemonAverageStats, PokemonRawStats}
import play.api.Logger
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection
import repository.{PokemonAverageStatsRepository, PokemonRepository}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PokemonAverageStatsRepositoryReal @Inject()(
                                                  mongo: ReactiveMongoApi,
                                                  pokemonRepository: PokemonRepository
                                                 )
                                                 (implicit ec: ExecutionContext)
extends PokemonAverageStatsRepository {

  private val logger: Logger = Logger(this.getClass)

  private val collection: Future[JSONCollection] = mongo.database.map(_.collection[JSONCollection](repositoryName))

  override def findByPokemonName(name: String): Future[List[PokemonAverageStats]] =
    for {
      c <- collection
      pokemon <- pokemonRepository.findOneByName(name)
      jsonArrayTypes = Json.toJson(pokemon.types)
      rawStatsByType <-
        c.find(Json.obj("type" -> Json.obj("$in" -> jsonArrayTypes))).cursor[PokemonRawStats]().collect[List]()
    } yield rawStatsByType.flatMap(PokemonAverageStats(pokemon, _))

  collection.foreach { c =>
    c.indexesManager.ensure(
      MongoIndex.ensureIndex(Seq("type" -> IndexType.Ascending), background = true, unique = true)
    )
  }
}
