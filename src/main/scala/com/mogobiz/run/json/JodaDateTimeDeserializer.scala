/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.run.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{DeserializationContext, JsonDeserializer}
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}

/**
 * Created by yoannbaudy on 13/11/2014.
 */
class JodaDateTimeDeserializer extends JsonDeserializer[DateTime] {

  val fmt : DateTimeFormatter = ISODateTimeFormat.dateTimeParser()

  override def deserialize(p1: JsonParser, p2: DeserializationContext): DateTime = {
    val v = p1.getValueAsString()
    if (v != null) fmt.parseDateTime(v)
    else null;
  }
}

class JodaDateTimeOptionDeserializer extends JsonDeserializer[Option[DateTime]] {

  val fmt : DateTimeFormatter = ISODateTimeFormat.dateTimeParser()

  override def deserialize(p1: JsonParser, p2: DeserializationContext): Option[DateTime] = {
    val v = p1.getValueAsString()
    if (v != null) Some(fmt.parseDateTime(v))
    else None;
  }

  override def getNullValue: Option[DateTime] = None

}
