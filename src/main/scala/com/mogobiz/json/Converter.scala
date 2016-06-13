/*
 * Copyright (C) 2015 Mogobiz SARL. All rights reserved.
 */

package com.mogobiz.json

import java.io._

/**
 * Generic Object Converter
 * Binary converter based on Java standard serializer
 * A performance improvement would be to rely on https://code.google.com/p/kryo/
 *
 * JSON converter based on jackson scala module
 */
trait Converter[T] {
  def toDomain[T: Manifest](obj: Array[Byte]): T

  def fromDomain[T: Manifest](value: T): Array[Byte]
}

trait BinaryConverter[T] extends Converter[T] {
  def toDomain[T: Manifest](obj: Array[Byte]): T = safeDecode(obj)

  def fromDomain[T: Manifest](value: T): Array[Byte] = {
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

