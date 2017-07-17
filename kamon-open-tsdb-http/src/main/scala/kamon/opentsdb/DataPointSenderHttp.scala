package kamon.opentsdb

import akka.actor.ActorSystem
import io.circe.syntax._
import mfms.http.JsonDataPointClient
import mfms.json.ScoringJsonHelpers._
import org.slf4j.LoggerFactory

import scala.util.{Failure, Success}

class DataPointSenderHttp(host: String, port: Int)(implicit system: ActorSystem) extends DataPointSender {

    implicit val executionContext = system.dispatcher

    val logger = LoggerFactory.getLogger(classOf[DataPointSenderHttp])

    val clientHttp = new JsonDataPointClient(host = host, port = port)

    override def appendPoint(dataPoint: DataPoint): Unit = {
        //logger debug s"sending dataPoint : ${dataPoint.asJson.spaces4}"


        val parts = dataPoint.metric.split("/")
        if (parts.length == 3) {
            val _dataPoint = dataPoint.copy(metric = parts(1))
            if (parts(2) != "rate")
                clientHttp.putDataPoint(dataPoint = _dataPoint) onComplete {
                    case Success(httpResponse) ⇒
                        logger debug s"sended dataPoint : ${_dataPoint.asJson.spaces4}, status: ${httpResponse.status.value}, isSuccess: ${httpResponse.status.isSuccess()}\n\n"

                    case Failure(e) ⇒
                        logger error(e.getMessage, e)
                }
        } else {
            logger warn s"dataPoint.metric: ${dataPoint.metric}  have more then 2 slash"
        }
    }

    override def flush(): Unit = {
    }

    override def shutdown(): Unit = {
        flush()
    }
}
