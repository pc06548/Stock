package utils

import java.time.LocalDate

import org.scalatest.{FunSpec, Matchers}

class LocalDateUtilTest extends FunSpec with Matchers {

  describe("LocalDateUtilTest") {

    it("should rangeInclusive") {
      LocalDateUtil.rangeInclusive(LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-14")).length should be(3)
      LocalDateUtil.rangeInclusive(LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-12")).length should be(1)
      LocalDateUtil.rangeInclusive(LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-10")).length should be(0)
    }

    it("should formatDDMMYY") {
      LocalDateUtil.formatDDMMYY(LocalDate.parse("2020-12-14")) should be("141220")
    }

  }
}
