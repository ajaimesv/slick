package demo.persistence

import slick.jdbc.PostgresProfile.api._

class Datasource {

  val db = Database.forConfig("my-database")

}
