package com.dateConvenience

import org.joda.time.DateTime
import org.scalatest.FunSuite

class DateConvenienceOverrideDefaultsTest extends FunSuite {

  test("Override default time print pattern") {
    val dateConvenienceWithCustomDefaults = new DateConvenience {
      override def defaultDateTimePrintPattern: String = "MMM-dd-yyyy"
    }

    import dateConvenienceWithCustomDefaults._

    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)

    assert(%%(dateTime) == "Feb-05-1999")

  }

  test("Override default time format pattern") {
    val dateConvenienceWithCustomDefaults = new DateConvenience {
      override def defaultDateTimeFormatPatterns: Array[String] = Array("MMM-dd-yyyy")
    }

    import dateConvenienceWithCustomDefaults._

    val dateTime = new DateTime(1999, 2, 5, 0, 0, 0, 0)

    assert(%%("Feb-05-1999").getMillis == dateTime.getMillis)

  }
}
