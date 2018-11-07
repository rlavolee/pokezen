package helpers

import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONDocument

object MongoIndex {
  def ensureIndex(key: Seq[(String, IndexType)],
                  name: Option[String] = None,
                  unique: Boolean = false,
                  background: Boolean = false,
                  dropDups: Boolean = false,
                  sparse: Boolean = false,
                  version: Option[Int] = None,
                  partialFilter: Option[BSONDocument] = None,
                  options: BSONDocument = BSONDocument()) = Index(key, name, unique, background, dropDups, sparse, version, partialFilter, options)
}
