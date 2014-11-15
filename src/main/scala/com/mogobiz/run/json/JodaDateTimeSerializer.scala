package com.mogobiz.run.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{SerializerProvider, JsonSerializer}
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}

/**
 * Created by yoannbaudy on 13/11/2014.
 */
class JodaDateTimeSerializer extends JsonSerializer[DateTime] {

  val fmt : DateTimeFormatter = ISODateTimeFormat.dateTime()

  override def serialize(p1: DateTime, p2: JsonGenerator, p3: SerializerProvider): Unit = {
    if (p1 != null) {
      p2.writeString(fmt.print(p1))
    }
    else p2.writeNull()
  }
}

class JodaDateTimeOptionSerializer extends JsonSerializer[Option[DateTime]] {

  val fmt : DateTimeFormatter = ISODateTimeFormat.dateTime();

  override def serialize(p1: Option[DateTime], p2: JsonGenerator, p3: SerializerProvider): Unit = {
    if (p1.isDefined) {
      p2.writeString(fmt.print(p1.get))
    }
    else p2.writeNull()
  }
}