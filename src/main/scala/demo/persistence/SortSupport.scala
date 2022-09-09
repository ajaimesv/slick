package demo.persistence

import slick.ast.Ordering
import slick.ast.Ordering.Direction
import slick.lifted.{ColumnOrdered, Ordered, Query, Rep}

object SortSupport {

  type ColumnOrdering = (String, Direction)

  trait ColumnSelector {
    val sortable: Map[String, Rep[_]]
  }

  /*
   * Allow multi-column order-by operations.
   */
  implicit class MultipleSortQuery[A <: ColumnSelector, B, C[_]](query: Query[A, B, C]) {

    def multipleSortBy(sortBy: Seq[ColumnOrdering]): Query[A, B, C] =
      sortBy.foldRight(query) {
        case ((sortColumn, sortOrder), queryToSort) =>
          val sortOrderRep: Rep[_] => Ordered = ColumnOrdered(_, Ordering(sortOrder))
          val sortColumnRep: A => Rep[_] = _.sortable(sortColumn)
          queryToSort.sortBy(sortColumnRep)(sortOrderRep)
      }

  }

}
