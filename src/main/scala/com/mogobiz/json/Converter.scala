/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.json

import java.io._

import com.fasterxml.jackson.databind.{
  DeserializationFeature,
  SerializationFeature
}
import com.fasterxml.jackson.datatype.joda.JodaModule
import org.json4s.ext.JodaTimeSerializers
import org.json4s.jackson.JsonMethods
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write, writePretty}
import org.json4s.{DefaultFormats, Formats, JValue}

trait Converter[T] {
  def toDomain[T: Manifest](obj: Array[Byte]): T

  def fromDomain[T <: AnyRef: Manifest](value: T): Array[Byte]
}

trait BinaryConverter[T] extends Converter[T] {
  def toDomain[T: Manifest](obj: Array[Byte]): T = safeDecode(obj)

  def fromDomain[T <: AnyRef: Manifest](value: T): Array[Byte] = {
    val bos = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(new BufferedOutputStream(bos))
    out writeObject (value)
    out close ()
    bos toByteArray ()
  }

  def safeDecode[T: Manifest](bytes: Array[Byte]) = {
    val cl = Option(this.getClass().getClassLoader())
    val cin = cl match {
      case Some(cls) =>
        new CustomObjectInputStream(new ByteArrayInputStream(bytes), cls)
      case None =>
        new ObjectInputStream(new ByteArrayInputStream(bytes))
    }
    val obj = cin.readObject
    cin.close
    obj.asInstanceOf[T]
  }
}

trait JSONConverter[T] extends Converter[T] {
  def toDomain[T: Manifest](bytes: Array[Byte]): T = {
    val x: Option[T] = None
    JacksonConverter.deserialize[T](new String(bytes))
  }

  def fromDomain[T <: AnyRef: Manifest](value: T): Array[Byte] = {
    JacksonConverter.serialize(value) toCharArray () map (_.toByte)
  }
}

// http://www.baeldung.com/jackson-serialize-dates
object JacksonConverter {
  JsonMethods.mapper
    .registerModule(new JodaModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
  //.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

  implicit def json4sFormats: Formats =
    DefaultFormats ++ JodaTimeSerializers.all

  def serializePretty(value: AnyRef): String = writePretty(value)

  def serialize(value: AnyRef): String = write(value)

  def deserialize[T: Manifest](json: String): T = read[T](json)

  def asString(value: JValue): String = compact(render(value))

  def parse(json: String): JValue = JsonMethods.parse(json)
}
