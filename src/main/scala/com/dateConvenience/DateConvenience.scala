package com.dateConvenience

import java.sql.Timestamp
import java.util.Calendar

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter, DateTimeFormatterBuilder, DateTimeParser}

import scala.language.implicitConversions

/**
  * Created by yanird on 25/07/2016, 0025.
  */

/**
  * DateConvenience makes date and time handling easier.
  *
  * Usage:
  * ======
  *
  * {{{
  *    import DateConvenience._
  *
  *    val dateTime: DateTime = %%("01/02/2012 24:01:02")
  *
  *    val timeStr: String = %%(dateTime)
  *
  *    val dateTime2: DateTime = dateTime + 5 day
  *
  *    if (dateTime < dateTime2) ...
  *
  *    // See more in README.md
  * }}}
  *
  */

object DateConvenience extends DateConvenience


trait DateConvenience {
  /** getDefaultDateTimeFormatPatterns
    * defines the default date-time patterns that can be parsed.
    * can be overridden if other/additional default formats are wanted. e.g.:
    * {{{
    *     override def getDefaultDateTimeFormatPatterns: Array[String] =
    *       super.getDefaultDateTimeFormatPatterns ++ Array("MMM-dd-yyyy")
    * }}}
    * See Joda-Time documentation for details about pattern syntax.
    */
  protected def defaultDateTimeFormatPatterns: Array[String] = Array(
    "dd/MM/yy HH:mm:ss",
    "dd/MM/yy HH:mm",
    "dd/MM/yy")

  /** getDefaultDateTimePrintPattern
    * defines the default date-time pattern to print to String.
    * can be overridden if other default format is wanted. e.g.:
    * {{{
    *     override protected def getDefaultDateTimePrintPattern: String = "MMM-dd-yyyy"
    * }}}
    * See Joda-Time documentation for details about pattern syntax.
    */
  protected def defaultDateTimePrintPattern: String = "dd/MM/yyyy HH:mm:ss"

  private val parsers: Array[DateTimeParser] =
    defaultDateTimeFormatPatterns.map(DateTimeFormat.forPattern(_).getParser)

  private val printer = DateTimeFormat.forPattern(defaultDateTimePrintPattern).getPrinter

  private val fmt: DateTimeFormatter = new DateTimeFormatterBuilder().append(printer, parsers).toFormatter

  /**
    * Implicit conversions between java Calendar and joda DateTime:
    */
  implicit def calendarToDateTime(c: Calendar): DateTime = if (c==null) null else new DateTime(c)

  implicit def dateTimeToCalendar(dt: DateTime): Calendar = if (dt==null) null else dt.toGregorianCalendar

  /**
    * Implicit conversions between java.sql.Timestamp and joda DateTime:
    */
  implicit def timestampToDateTime(ts: Timestamp): DateTime = if (ts==null) null else new DateTime(ts)

  implicit def dateTimeToTimestamp(dt: DateTime): Timestamp = if (dt==null) null else new Timestamp(dt.getMillis)

  /**
    * String to DateTime and vice-versa converters
    * Usage:
    * {{{
    *   val dateTime: DateTime = %%("01/02/2012 24:01:02")
    *   val dateTime2: DateTime = %%("Feb-01-2012", "MMM-dd-yyyy")
    *
    *   val timeStr: String = %%(new DateTime())
    *   //To use
    *   val timeStr2: String = %%(new DateTime(), "MMM-dd-yyyy")
    * }}}
    *
    * Can also be used with java Calendar directly due to implicit conversion:
    * {{{
    *   val c: Calendar = %%("01/02/2012 24:01:02")
    *   val timeStr: String = %%(Calendar.getInstance)
    * }}}
    */
  def %%(timeStr: String, patternStr: String = null) = stringToDateTime(timeStr, patternStr)

  //%% can't be overloaded with default parameters, so splitting to two functions:
  def %%(dateTime: DateTime, patternStr: String) = dateTimeToString(dateTime, patternStr)

  def %%(dateTime: DateTime) = dateTimeToString(dateTime)

  def stringToDateTime(timeStr: String, patternStr: String = null) = {
    patternStr match {
      //parse by default pattern:
      case null => fmt.parseDateTime(timeStr)
      //parse by given pattern:
      case _ => DateTimeFormat.forPattern(patternStr).parseDateTime(timeStr)
    }
  }

  def dateTimeToString(dateTime: DateTime, patternStr: String = null) = {
    patternStr match {
      //format by default pattern:
      case null => fmt.print(dateTime)
      //format by given pattern:
      case _ => DateTimeFormat.forPattern(patternStr).print(dateTime)
    }
  }

  /**
    * Add functionality to DateTime. Allows addition, subtraction, comparison. e.g:
    * {{{
    *   val dateTime1: DateTime = ...
    *   val dateTime2: DateTime = dateTime1 + 5 days
    *   val dateTime3: DateTime = dateTime1 - 5 years
    *   if (dateTime1 < dateTime2 && dateTime1 >= dateTime3) ...
    * }}}
    */
  implicit class DateTimeWrapper(dateTime: DateTime) {

    case class TimeAdder(amount: Int) {
      def years = dateTime.plusYears(amount)

      def months = dateTime.plusMonths(amount)

      def weeks = dateTime.plusWeeks(amount)

      def days = dateTime.plusDays(amount)

      def hours = dateTime.plusHours(amount)

      def minutes = dateTime.plusMinutes(amount)

      def seconds = dateTime.plusSeconds(amount)

      def millis = dateTime.plusMillis(amount)
    }

    def +(amount: Int): TimeAdder = TimeAdder(amount)

    def -(amount: Int): TimeAdder = TimeAdder(-amount)

    //Comparisons:

    def >(other: DateTime) = other.isBefore(dateTime)

    def <(other: DateTime) = other.isAfter(dateTime)

    def >=(other: DateTime) = this > other || other == dateTime

    def <=(other: DateTime) = this < other || other == dateTime

    //scala allows comparing like so natively:
    //def ==(other: DateTime) = other.isEqual(dateTime)
    //def !=(other: DateTime) = !other.isEqual(dateTime)

    // however scala == and != doesn't compare DateTime to Calendar,
    // and they cannot be overridden with the above commented defs, so defining
    def isNotEqual(other: DateTime) = ! other.isEqual(dateTime)
    //isEqual is already defined in DateTime

    def truncate = {
      dateTime
          .withHourOfDay(0)
          .withMinuteOfHour(0)
          .withSecondOfMinute(0)
          .withMillisOfSecond(0)
    }

    def differenceInMonths(other: DateTime) =
      (12*dateTime.getYear + dateTime.getMonthOfYear) -
        (12*other.getYear + other.getMonthOfYear)
  }

  //Allow also Calendar to use DateTimeWrapper operations:
  implicit def CalendarToDateTimeWrapper(c: Calendar): DateTimeWrapper = new DateTimeWrapper(c)

}

/**
  * implicit class to convert string to dateTime (by calling toDateTime). Use
  * import ImplicitStringToDateTime._
  */
object ImplicitStringToDateTime{
  implicit class StringToDateTimeConverter(val timeStr: String) extends AnyVal{
    def toDateTime = {
      DateConvenience.stringToDateTime(timeStr)
    }
  }
}

