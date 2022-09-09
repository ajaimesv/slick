package demo.persistence

import slick.jdbc.H2Profile.api._
import slick.lifted.{Query, Rep}

object FilterSupport {

  type ColumnFiltering = (String, String, Any)

  trait ColumnSelector {
    val filterable: Map[String, Rep[_]]
  }

  /*
   * Allow multi-filter operations.
   */
  implicit class MultipleFilterQuery[A <: ColumnSelector, B, C[_]](query: Query[A, B, C]) {

    def op(rep: Rep[String], operation: String, value: String) =
      operation match {
        case "==" => rep === value
        case "!=" => rep =!= value
        case ">" => rep > value
        case "<" => rep < value
        case ">=" => rep >= value
        case "<=" => rep <= value
      }

    def op(rep: Rep[Int], operation: String, value: Int) =
      operation match {
        case "==" => rep === value
        case "!=" => rep =!= value
        case ">" => rep > value
        case "<" => rep < value
        case ">=" => rep >= value
        case "<=" => rep <= value
      }

    def op(rep: Rep[Boolean], operation: String, value: Boolean) =
      operation match {
        case "==" => rep === value
        case "!=" => rep =!= value
        case ">" => rep > value
        case "<" => rep < value
        case ">=" => rep >= value
        case "<=" => rep <= value
      }

    def opss(rep: Rep[String], operation: String, values: Seq[String]) =
      operation match {
        case "in" => rep inSet values
      }

    def opsi(rep: Rep[Int], operation: String, values: Seq[Int]) =
      operation match {
        case "in" => rep inSet values
        case "between" => rep between (values(0), values(1))
      }

    def buildCondition(rep: Rep[_], operation: String, value: Any) =
      value match {
        case v: String => op(rep.asInstanceOf[Rep[String]], operation, v)
        case v: Int => op(rep.asInstanceOf[Rep[Int]], operation, v)
        case v: Boolean => op(rep.asInstanceOf[Rep[Boolean]], operation, v)
        case v: Seq[_] => v.head match {
          case _: String => opss(rep.asInstanceOf[Rep[String]], operation, v.asInstanceOf[Seq[String]])
          case _: Int => opsi(rep.asInstanceOf[Rep[Int]], operation, v.asInstanceOf[Seq[Int]])
        }
      }

    def multipleFilterBy(filterBy: Seq[ColumnFiltering]): Query[A, B, C] =
      filterBy.foldRight(query) {
        case ((filterColumn, operation, value), queryToFilter) =>
          val filterColumnRep: A => Rep[_] = _.filterable(filterColumn)
          val condition = filterColumnRep andThen { rep => buildCondition(rep, operation, value) }
          queryToFilter.filter(condition)
      }

  }

}
