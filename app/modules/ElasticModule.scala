package modules

import javax.inject.{Inject, Singleton}

import com.google.inject.AbstractModule
import com.sksamuel.elastic4s.http.{ElasticProperties, ElasticClient => ElasticClient4s}
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

@Singleton
class ElasticClient @Inject()(lifecycle: ApplicationLifecycle, configuration: Configuration) {

  // connect to elastic database

  private val protocol: String = configuration.getString("elastic.protocol").getOrElse("http")
  private val host: String = configuration.getString("elastic.host").getOrElse("localhost")
  private val port: Int = configuration.getInt("elastic.port").getOrElse(9200)

  val client = ElasticClient4s(ElasticProperties(s"$protocol://$host:$port"))

  lifecycle.addStopHook { () =>
    Future.successful(client.close())
  }
}

class ElasticModule extends AbstractModule {
  override def configure() =
    bind(classOf[ElasticClient]).asEagerSingleton()
}
