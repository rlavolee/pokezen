package repository.real

import javax.inject.{Inject, Singleton}

import exceptions.NoPokemonFoundException
import helpers.MongoIndex
import play.api.Logger
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.indexes.IndexType
import reactivemongo.play.json.collection.JSONCollection
import repository.PokemonLikeRepository
import models.pokemon.like.PokemonLike
import pokeapi.client.PokeApiClient
import reactivemongo.play.json._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PokemonLikeRepositoryReal @Inject()(
                                pokeApi: PokeApiClient,
                                mongo: ReactiveMongoApi
                               )(implicit ec: ExecutionContext)
extends PokemonLikeRepository {

  private val logger: Logger = Logger(this.getClass)

  private val collection: Future[JSONCollection] = mongo.database.map(_.collection[JSONCollection](repositoryName))

  private def nameSelector(name: String): JsObject = Json.obj("name" -> name)

  override def findOneByName(name: String): Future[PokemonLike] =
    for {
      c <- collection
      pokemon <- c.find(nameSelector(name)).one[PokemonLike]
        .flatMap{ oPokemon =>
          if (oPokemon.isDefined) Future.successful(oPokemon.get)
          else pokeApi.Pokemon.get(name).map(_ => PokemonLike(name))
        }
    } yield pokemon

  override def save(pokemonLike: PokemonLike): Future[PokemonLike] =
    for {
    c <- collection
    writeResult <- c.update(nameSelector(pokemonLike.name), pokemonLike, upsert = true)
  } yield {
    if (writeResult.hasErrors) logger.error(writeResult.message)
    pokemonLike
  }

  override def like(name: String): Future[PokemonLike] =
    for {
      pokemon <- findOneByName(name)
      likedPokemon = pokemon.copy(like = pokemon.like + 1)
      savedPokemon <- save(likedPokemon)
    } yield savedPokemon

  override def dislike(name: String): Future[PokemonLike] =
    for {
      pokemon <- findOneByName(name)
      likedPokemon = pokemon.copy(like = pokemon.like - 1)
      savedPokemon <- save(likedPokemon)
    } yield savedPokemon

  collection.foreach { c =>
    c.indexesManager.ensure(
      MongoIndex.ensureIndex(Seq("name" -> IndexType.Ascending), background = true, unique = true)
    )
  }
}
