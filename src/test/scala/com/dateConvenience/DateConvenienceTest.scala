package com.dateConvenience

import java.sql.Timestamp
import java.util.Calendar

import org.joda.time.DateTime
import org.scalatest.FunSuite

import scala.languageFeature.postfixOps

class DateConvenienceTest extends FunSuite {

  import DateConvenience._

  // DateTime to String
  //-------------------------------------------------------------------------
  test("DateTime to String (default format)") {
    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)

    assert(%%(dateTime) === "05/02/1999 13:40:34")
  }

  test("DateTime to String (custom format 1)") {
    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)

    assert(%%(dateTime,"MMM-dd-yyyy HH:mm:ss") === "Feb-05-1999 13:40:34")
  }

  test("DateTime to String (custom format 2, including millis)") {
    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)

    assert(%%(dateTime,"dd-MM-yy HH:mm:ss.SSS") === "05-02-99 13:40:34.768")
  }


  // String to DateTime
  //-------------------------------------------------------------------------
  test("String to DateTime (default format)") {
    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 0)

    assert(%%("05/02/1999 13:40:34").getMillis === dateTime.getMillis)
  }

  test("String to DateTime (custom format 1)") {
    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 0)

    assert(%%("Feb-05-1999 13:40:34","MMM-dd-yyyy HH:mm:ss").getMillis === dateTime.getMillis)
  }

  test("String to DateTime (custom format 2, including millis)") {
    val dateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)

    assert(%%("05-02-99 13:40:34.768","dd-MM-yy HH:mm:ss.SSS").getMillis === dateTime.getMillis)
  }


  //Addition/subtraction
  //-------------------------------------------------------------------------
  test("Add/subtract years") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(2004, 2, 5, 13, 40, 34, 768)

    assert((dateTime1 + 5 years).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 years).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract months") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 7, 5, 13, 40, 34, 768)

    assert((dateTime1 + 5 months).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 months).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract weeks") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 2, 12, 13, 40, 34, 768)

    assert((dateTime1 + 1 weeks).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 1 weeks).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract days") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 2, 10, 13, 40, 34, 768)

    assert((dateTime1 + 5 days).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 days).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract hours") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 2, 5, 18, 40, 34, 768)

    assert((dateTime1 + 5 hours).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 hours).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract minutes") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 2, 5, 13, 45, 34, 768)

    assert((dateTime1 + 5 minutes).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 minutes).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract seconds") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 2, 5, 13, 40, 39, 768)

    assert((dateTime1 + 5 seconds).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 seconds).getMillis === dateTime1.getMillis)
  }

  test("Add/subtract milliseconds") {
    val dateTime1 = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2 = new DateTime(1999, 2, 5, 13, 40, 34, 773)

    assert((dateTime1 + 5 millis).getMillis === dateTime2.getMillis)
    assert((dateTime2 - 5 millis).getMillis === dateTime1.getMillis)
  }

  //Comparisons (and implicit conversions between DateTime and Calendar)
  //-------------------------------------------------------------------------
  test("isEqual (with implicit conversion)") {
    val dateTime: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val calendar: Calendar = dateTime.toGregorianCalendar

    assert(dateTime.isEqual(calendar))
    assert(calendar.isEqual(dateTime))
  }

  test("isNotEqual") {
    val dateTime1: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 769)

    assert(dateTime1.isNotEqual(dateTime2))
    assert(dateTime2.isNotEqual(dateTime1))
  }

  test("isNotEqual  (with implicit conversion)") {
    val dateTime: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val calendar: Calendar = new DateTime(1999, 2, 5, 13, 40, 34, 769).toGregorianCalendar

    assert(dateTime.isNotEqual(calendar))
    assert(calendar.isNotEqual(dateTime))
  }

  test("Less then") {
    val dateTime1: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 769)

    assert(dateTime1 < dateTime2)
    assert(!(dateTime2 < dateTime1))
  }

  test("Less then or equal") {
    val dateTime1: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 769)

    assert(dateTime1 <= dateTime2)
    assert(!(dateTime2 <= dateTime1))
    assert(dateTime1 <= dateTime1)
    assert(dateTime2 <= dateTime2)
  }

  test("Greater then") {
    val dateTime1: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 769)

    assert(dateTime2 > dateTime1)
    assert(!(dateTime1 > dateTime2))
  }

  test("Greater then or equal") {
    val dateTime1: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val dateTime2: DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 769)

    assert(dateTime2 >= dateTime1)
    assert(!(dateTime1 >= dateTime2))
    assert(dateTime1 >= dateTime1)
    assert(dateTime2 >= dateTime2)
  }

  //Implicit conversion between DateTime and Calendar
  test("Calendar to DateTime") {
    val calendar : Calendar = Calendar.getInstance
    val dateTime : DateTime = calendar

    assert(dateTime.getMillis === calendar.getTimeInMillis)
  }

  test("DateTime to Calendar") {
    val dateTime : DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val calendar : Calendar = dateTime
    assert(calendar.getTimeInMillis === dateTime.getMillis)
  }

  test("Calendar (null) to DateTime") {
    val calendar : Calendar = null
    val dateTime : DateTime = calendar

    assert(dateTime === null)
  }

  test("DateTime (null) to Calendar") {
    val dateTime : DateTime = null
    val calendar : Calendar = dateTime
    assert(calendar === null)
  }

  //Implicit conversion between DateTime and Timestamp
  test("Timestamp to DateTime") {
    val dateTime : DateTime = new Timestamp(100)
    assert(dateTime.getMillis === 100)
  }

  test("DateTime to Timestamp") {
    val dateTime : DateTime = new DateTime(1999, 2, 5, 13, 40, 34, 768)
    val timestamp : Timestamp = dateTime
    assert(timestamp.getTime === dateTime.getMillis)
  }

  test("Timestamp (null) to DateTime") {
    val timestamp : Timestamp = null
    val dateTime : DateTime = timestamp
    assert(dateTime === null)
  }

  test("DateTime (null) to Timestamp") {
    val dateTime : DateTime = null
    val timestamp : Timestamp = dateTime
    assert(timestamp === null)
  }
}
