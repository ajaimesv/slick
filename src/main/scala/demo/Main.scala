package demo

import com.typesafe.scalalogging.LazyLogging
import demo.persistence.{ContactDao, Datasource}

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App with LazyLogging {

  val ds: Datasource = new Datasource()
  val dao: ContactDao = new ContactDao()

  val records = Seq(
    ("paul", 1960, "liverpool", "england"),
    ("john", 1960, "liverpool", "england"),
    ("george", 1960, "liverpool", "england"),
    ("ringo", 1960, "liverpool", "england"),
    ("bono", 1976, "dublin", "ireland"),
    ("the edge", 1976, "dublin", "ireland"),
    ("adam", 1976, "dublin", "ireland"),
    ("larry", 1976, "dublin", "ireland"),
    ("mick", 1962, "london", "england"),
    ("brian", 1962, "london", "england"),
    ("keith", 1962, "london", "england"),
    ("bill", 1962, "london", "england"),
    ("charlie", 1962, "london", "england"),
    ("axl", 1985, "los angeles", "usa"),
    ("slash", 1985, "los angeles", "usa"),
    ("izzy", 1985, "los angeles", "usa"),
    ("duff", 1985, "los angeles", "usa"),
    ("steven", 1985, "los angeles", "usa"),
  )

  logger.debug(s"Setting up table")
  Await.result(ds.db.run(dao.createSchema), 2.seconds)

  logger.debug(s"Adding data")
  val rowCount = Await.result(ds.db.run(dao.create(records)), 2.seconds)
  logger.debug(s"${rowCount.getOrElse(0)} rows added")

  logger.debug(s"Querying")
  // select "id", "name", "memberSince", "city", "state" from "contacts" limit 3 offset 1
  Await.result(ds.db.run(dao.read(1, 3)), 2.seconds)
  // select "id", "name", "memberSince", "city", "state" from "contacts" where "name" = 'george'
  Await.result(ds.db.run(dao.read("george")), 2.seconds)
  // select "id", "name", "memberSince", "city", "state" from "contacts" order by "name"
  Await.result(ds.db.run(dao.read(Seq(("name", slick.ast.Ordering.Asc)))), 2.seconds)
  // select "id", "name", "memberSince", "city", "country" from "contacts" order by "country" desc, "city"
  Await.result(ds.db.run(dao.read(Seq(("country", slick.ast.Ordering.Desc), ("city", slick.ast.Ordering.Asc)))), 2.seconds)
  // select "id", "name", "memberSince", "city", "country" from "contacts" where "country" = 'usa'
  Await.result(ds.db.run(dao.readf(Seq(("country", "==", "usa")))), 2.seconds)
  // select "id", "name", "memberSince", "city", "country" from "contacts" where ("memberSince" > 1960) and ("country" = 'england')
  Await.result(ds.db.run(dao.readf(Seq(("country", "==", "england"), ("memberSince", ">", 1960)))), 2.seconds)
  // select "id", "name", "memberSince", "city", "country" from "contacts" where "memberSince" between 1970 and 1980
  Await.result(ds.db.run(dao.readf(Seq(("memberSince", "between", Seq(1970, 1980))))), 2.seconds)
  // select "id", "name", "memberSince", "city", "country" from "contacts" where "memberSince" in (1976)
  Await.result(ds.db.run(dao.readf(Seq(("memberSince", "in", Seq(1976))))), 2.seconds)

}
