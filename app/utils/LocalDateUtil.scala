package utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateUtil {

  def rangeInclusive(fromDate: LocalDate, toDate: LocalDate): Seq[LocalDate] = {
    Iterator.iterate(fromDate)(_.plusDays(1)).takeWhile(date => date.isBefore(toDate) || date.isEqual(toDate)).toList
  }

  def formatDDMMYY(date: LocalDate): String = {
    val pattern = DateTimeFormatter.ofPattern("ddMMyy")
    date.format(pattern)
  }

}
