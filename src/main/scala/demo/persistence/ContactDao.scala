package demo.persistence

import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery
import slick.ast.Ordering.Direction

class ContactDao {

  val contacts = TableQuery[ContactTable]

  def createSchema =
    contacts.schema.create

  def create(recs: Seq[(String, Int, String, String)]) = {
    val records = recs.map(r => ContactRecord(r._1, r._2, r._3, r._4))
    contacts ++= records
  }

  def read(offset: Int, limit: Int) = {
    contacts.drop(offset).take(limit).result
  }

  def read(name: String) = {
    contacts.filter(_.name === name).result
  }

  def read(order: Seq[(String, Direction)]) =
    contacts.multipleSortBy(order).result

  def readf(filter: Seq[(String, String, Any)]) =
    contacts.multipleFilterBy(filter).result

}
