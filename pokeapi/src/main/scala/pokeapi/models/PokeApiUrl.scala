package pokeapi.models

case class PokeApiUrl(value: String) extends AnyVal {
  override def toString: String = value
}