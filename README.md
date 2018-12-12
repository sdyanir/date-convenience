# date-convenience

Conveniently work with joda `DateTime` and Java `Calendar` (and also `java.sql.Timestamp`).

**Example:**
```scala
   import DateConvenience._
   
   val dateTime: DateTime = %%("01/02/2012 24:01:02")
   val timeStr: String = %%(dateTime) // "01/02/2012 24:01:02"
   val dateTime2: DateTime = dateTime + 5 days
   if (dateTime < dateTime2) ...

   // And more ...
```

The same example works also for Java `Calendar`. Different date-time patterns can be used.
## Features

* Implicit conversion between joda `DateTime` and Java `Calendar`
* Creation of `DateTime` objects from Strings:
```scala
  // Parse by one of the default patterns (defaults can be modified):
  val dateTime: DateTime = %%("01/02/2012 24:01:02")
  // Or provide a pattern:
  val dateTime2: DateTime = %%("Feb-01-2012", "MMM-dd-yyyy")
```

* Creation of Strings from `DateTime` objects:
```scala
  // According to default pattern (defaults can be modified):
  val timeStr: String = %%(new DateTime())
  // Or provide pattern:
  val timeStr2: String = %%(new DateTime(), "MMM-dd-yyyy")
```

* Addition/subtraction of time units to `DateTime`:
```scala
  val dateTime1: DateTime = ...
  val dateTime2: DateTime = dateTime1 + 5 days
  val dateTime3: DateTime = dateTime1 - 5 years
  val dateTime4: DateTime = dateTime1 - 5 seconds
```

* Comparison of `DateTime` objects:
```scala
  val dateTime1: DateTime = ...
  val dateTime2: DateTime = ...
  val dateTime3: DateTime = ...
  if (dateTime1 < dateTime2 && dateTime1 >= dateTime3) ...
```

## Default date-time patterns

The default print pattern (converting `DateTime` to `String`) is

* _dd/MM/yyyy HH:mm:ss_

To use another format, provide it to `%%`:

```scala
%%(new DateTime(), "MMM-dd-yyyy")
``` 
or extend `DateConvenience` and [override the default](#overriding-default-time-patterns).

The default parse patterns (converting `String` to `DateTime`) are

* _dd/MM/yyyy HH:mm:ss_
* _dd/MM/yyyy HH:mm_
* _dd/MM/yyyy_

When converting a `String`, these patterns are tried in that order and if none succeeds an error is thrown.

To use another format, provide it to `%%`:

```scala
%%("Feb-05-2016", "MMM-dd-yyyy")
``` 
or extend `DateConvenience` and [override the default](#overriding-default-time-patterns).

## Overriding default date-time patterns
To use other date-time patterns, the defaults can be overridden.

### Example: overriding date-time print pattern

```scala
val dateConvenienceWithCustomDefaults = new DateConvenience {
    override def defaultDateTimePrintPattern: String = "MMM-dd-yyyy"
}

import dateConvenienceWithCustomDefaults._

%%(new DateTime(1999, 2, 5, 13, 40, 34, 768)) // "Feb-05-1999"
```

### Example: overriding date-time parse patterns

There can be several patterns defined in an `Array` - they will be tried one after the other when converting `DateTime` to `String`.

```scala
val dateConvenienceWithCustomDefaults = new DateConvenience {
    override def defaultDateTimeFormatPatterns: Array[String] = Array("MMM-dd-yyyy")
}

import dateConvenienceWithCustomDefaults._

%%("Feb-05-1999") // No need to provide the pattern since it is the default now
```
