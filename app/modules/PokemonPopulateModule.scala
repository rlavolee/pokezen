package modules

import javax.inject.{Inject, Singleton}

import com.google.inject.AbstractModule
import com.sksamuel.elastic4s.RefreshPolicy
import play.api.Configuration
import pokeapi.client.PokeApiClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PokemonPopulate @Inject()(pokeApi: PokeApiClient, elastic: ElasticClient, configuration: Configuration)(implicit ec: ExecutionContext) {

  // Populate pokemon names in elastic search

  private val indexName: String = configuration.getString("elastic.index")
    .getOrElse(throw new Exception("Index required in conf file"))

  private val docTypeName: String = configuration.getString("elastic.type")
    .getOrElse(throw new Exception("Type required in conf file"))

  configuration.getBoolean("pokemon.populate").foreach{
    case true =>
      import com.sksamuel.elastic4s.http.ElasticDsl._
      elastic.client.execute {
        createIndex(indexName).mappings(
          mapping(docTypeName).fields(
            textField("name")
          )
        )
      }.map{ _ =>
        for {
          pokemons <- pokeApi.Pokemon.list()
        } yield
          pokemons.body.results.map { pokemon =>
            elastic.client.execute {
              indexInto(indexName / docTypeName).fields("name" -> pokemon.name).refresh(RefreshPolicy.Immediate)
            }.await
          }
      }

    case false => ()
  }
}

class PokemonPopulateModule extends AbstractModule {
  override def configure() =
    bind(classOf[PokemonPopulate]).asEagerSingleton()
}