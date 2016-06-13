package com.mogobiz.json

import org.json4s.ext.JodaTimeSerializers
import org.json4s.{ DefaultFormats, jackson }

object Implicits {

  implicit val serialization = jackson.Serialization
  implicit val formats = DefaultFormats ++ JodaTimeSerializers.all

  //  implicit val mapMarshaller: ToEntityMarshaller[Map[Symbol, String]] = Marshaller.opaque { map =>
  //    HttpEntity(ContentType(MediaTypes.`application/json`), serialization.write(map))
  //  }
}