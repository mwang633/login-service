package com.example

import java.sql.Timestamp

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.http.{ DateTime => SprayDateTime }

object Util {

  val parser = ISODateTimeFormat.dateTimeParser()

  implicit class TimestampEx(ts: Timestamp) {
    def toDateTime = new DateTime(ts.getTime)
  }

  implicit class DateTimeEx(dateTime: DateTime) {
    def toTimestamp = new Timestamp(dateTime.getMillis)
    def toSprayDateTime = SprayDateTime(dateTime.getMillis)

    def truncDay() = dateTime.withMillisOfDay(0)
  }

  def parseTimestamp(s: String): Timestamp = parser.parseDateTime(s).toTimestamp
}
