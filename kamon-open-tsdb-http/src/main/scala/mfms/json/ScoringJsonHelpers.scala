package mfms.json

import io.circe._
import io.circe.syntax._
import kamon.opentsdb.DataPoint

object ScoringJsonHelpers {


    implicit val enecodingQuery: Encoder[DataPoint] = Encoder.instance { dataPoint =>

        val value: JsonNumber = JsonNumber.fromDecimalStringUnsafe(dataPoint.value.toString)

        Json.obj(
            Seq("metric" -> Json.fromString(dataPoint.metric),
                "tags" -> dataPoint.tags.asJson,
                "timestamp" → Json.fromLong(dataPoint.timestamp),
                "value" → value.asJson
            ): _*
        )
    }

    /*implicit val decodingQuery: Decoder[DataPoint] = Decoder.instance { cursor =>
        for {
            metric ← cursor.downField("metric").as[String].right
            tags ← cursor.downField("tags").as[Map[String, String]].right
            timestamp ← cursor.downField("timestamp").as[Long].right
            value ← cursor.downField("value").as[BigDecimal].right
        } yield DataPoint(metric, tags, timestamp, value.asInstanceOf[AnyVal])
    }*/
}
