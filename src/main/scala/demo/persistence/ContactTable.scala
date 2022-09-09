package demo.persistence

import slick.jdbc.H2Profile.api._

class ContactTable(tag: Tag)
  extends Table[ContactRecord](tag, "contacts")
  with SortSupport.ColumnSelector
  with FilterSupport.ColumnSelector {

  def id = column[String]("id", O.PrimaryKey)
  def name = column[String]("name")
  def memberSince = column[Int]("memberSince")
  def city = column[String]("city")
  def country = column[String]("country")

  override def * =
    (id, name, memberSince, city, country) <> ((ContactRecord.mapperTo _).tupled, ContactRecord.unapply)

  override val sortable = Map(
    "name" -> this.name,
    "memberSince" -> this.memberSince,
    "city" -> this.city,
    "country" -> this.country
  )

  override val filterable = Map(
    "name" -> this.name,
    "memberSince" -> this.memberSince,
    "city" -> this.city,
    "country" -> this.country
  )

}
