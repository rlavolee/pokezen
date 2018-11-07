package models

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.{PathBindable, QueryStringBindable}
import play.api.data._
import play.api.data.Forms._

import scala.util.Try

case class PokezenId(value: UUID) extends AnyVal

object PokezenId {
  implicit val format: Format[PokezenId] = Format(
    Reads(_.validate[UUID].map(uuid => PokezenId(uuid))),
    Writes[PokezenId](v => JsString(v.value.toString))
  )

  def generate() = PokezenId(UUID.randomUUID())

  def fromString(s: String) = Try(PokezenId(UUID.fromString(s)))

  implicit def binder(implicit uuidBinder: PathBindable[UUID]) = new PathBindable[PokezenId] {
    override def bind(key: String, value: String): Either[String, PokezenId] = uuidBinder.bind(key, value).right.map(apply)

    override def unbind(key: String, value: PokezenId): String = uuidBinder.unbind(key, value.value)
  }

  val formMapping: Mapping[PokezenId] = mapping(
    "value" -> uuid
  )(PokezenId.apply)(PokezenId.unapply)
}