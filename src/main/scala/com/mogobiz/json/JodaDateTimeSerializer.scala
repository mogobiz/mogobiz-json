package com.mogobiz.json

/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{ JsonSerializer, SerializerProvider }
import org.joda.time.format.{ DateTimeFormatter, ISODateTimeFormat }
import org.joda.time.{ DateTime, DateTimeZone }

/**
 */
class JodaDateTimeSerializer extends JsonSerializer[DateTime] {

  val fmt: DateTimeFormatter = ISODateTimeFormat.dateTime()

  override def serialize(p1: DateTime, p2: JsonGenerator, p3: SerializerProvider): Unit = {
    if (p1 != null) {
      p2.writeString(fmt.print(p1.toDateTime(DateTimeZone.UTC)))
    } else p2.writeNull()
  }
}

class JodaDateTimeOptionSerializer extends JsonSerializer[Option[DateTime]] {

  val fmt: DateTimeFormatter = ISODateTimeFormat.dateTime()

  override def serialize(p1: Option[DateTime], p2: JsonGenerator, p3: SerializerProvider): Unit = {
    if (p1.isDefined) {
      p2.writeString(fmt.print(p1.get.toDateTime(DateTimeZone.UTC)))
    } else p2.writeNull()
  }
}
