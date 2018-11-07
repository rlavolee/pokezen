name := "pokezen"

version := "0.1.0"

scalaVersion in ThisBuild := "2.11.11"

lazy val pokeapi = project in file("pokeapi")

lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(pokeapi).aggregate(pokeapi)

routesImport += "models.query._"

routesImport += "models.PokezenId"

libraryDependencies ++= Seq(
	ws,
  filters,
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % Version.playReactiveMongo,
  "com.sksamuel.elastic4s" %% "elastic4s-core" % Version.elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-http" % Version.elastic4sVersion,
  specs2 % Test
)
