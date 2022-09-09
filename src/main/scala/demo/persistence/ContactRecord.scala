package demo.persistence

import java.util.UUID

case class ContactRecord(
  id: String,
  name: String,
  memberSince: Int,
  city: String,
  country: String,
)

object ContactRecord {

  def apply(name: String, memberSince: Int, city: String, country: String): ContactRecord =
    apply(
      id = UUID.randomUUID.toString,
      name = name,
      memberSince = memberSince,
      city = city,
      country = country
    )

  // a mapper is required when we have a companion object,
  // even if it does not have any `apply` methods.
  // otherwise our ContactTable instance will have issues with
  // instantiation.
  def mapperTo(id: String, name: String, memberSince: Int, city: String, country: String): ContactRecord =
    apply(id, name, memberSince, city, country)

}
