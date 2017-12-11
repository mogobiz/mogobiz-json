package com.mogobiz.json

import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.scalalogging.LazyLogging
import org.joda.time.DateTime
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

case class DateWithOption(name: String,
                          date: Date,
                          jodaDate: DateTime,
                          maybeDate: Option[Date],
                          maybeJodaDate: Option[DateTime])

class JacksonConverterSpec
    extends FlatSpec
    with Matchers
    with BeforeAndAfter
    with LazyLogging {
  "Date Serialization" should "work for Options" in {
    val df = new SimpleDateFormat("yyyy-MM-dd")
    val theDate = DateWithOption(
      "Sample",
      df.parse("2017-12-10"),
      DateTime.parse("2017-12-10T07:35:07Z"),
      Some(df.parse("2017-12-10")),
      Some(DateTime.parse("2017-12-10T07:35:07Z"))
    )
    val theDateAsJson = JacksonConverter.serialize(theDate)
    logger.info(theDateAsJson)
  }
  it should "deserialize correctly to Option" in {
    val json =
      """{
        |  "name" : "Sample",
        |  "date" : "2017-12-10T07:32:27Z",
        |  "jodaDate" : "2017-12-10T07:32:28Z",
        |  "maybeDate" : "2017-12-10T07:32:28Z",
        |  "maybeJodaDate" : "2017-12-10T07:32:28Z"
        |}""".stripMargin
    val theDate = JacksonConverter.deserialize[DateWithOption](json)
    theDate.maybeDate should be(a[Some[_]])
    theDate.maybeJodaDate should be(a[Some[_]])
  }
  it should "deserialize correctly null values to None" in {
    val json =
      """{
        |  "name" : "Sample",
        |  "date" : "2017-12-10T07:32:27Z",
        |  "jodaDate" : "2017-12-10T07:32:28Z",
        |  "maybeJodaDate" : null
        |}""".stripMargin
    val theDate = JacksonConverter.deserialize[DateWithOption](json)
    theDate.maybeDate should be(None)
    theDate.maybeJodaDate should be(None)
  }
}
