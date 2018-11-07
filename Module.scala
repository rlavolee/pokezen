import com.google.inject.AbstractModule
import java.time.Clock

import repository._
import repository.real._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[PokemonRepository]).to(classOf[PokemonRepositoryReal])
    bind(classOf[PokemonLikeRepository]).to(classOf[PokemonLikeRepositoryReal])
    bind(classOf[PokemonAverageStatsRepository]).to(classOf[PokemonAverageStatsRepositoryReal])
  }

}
