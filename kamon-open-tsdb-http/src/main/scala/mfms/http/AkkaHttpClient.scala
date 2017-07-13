package mfms.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Post
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import io.circe.syntax._
import kamon.opentsdb.DataPoint
import mfms.json.CirceSupport._
import mfms.json.ScoringJsonHelpers._
import org.slf4j.LoggerFactory
import ru.mfms.kamon.Config

import scala.concurrent.Future

class AkkaHttpClient(host: String, port: Int)(implicit system: ActorSystem) extends Config {

    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val httpClient = Http().outgoingConnection(host = host, port = port)

    private def sendAndReceive[T](request: HttpRequest): Future[HttpResponse] =
        Source.single(request)
          .via(httpClient)
          .mapAsync(getInt("kamon.opentsdb.http.parallelism")) {
              response ⇒
                  Future(response)
          }
          .runWith(Sink.head)

    def sendAndReceiveAs[T](httpRequest: HttpRequest): Future[HttpResponse] = sendAndReceive(httpRequest)
}

class JsonDataPointClient(host: String, port: Int)(implicit system: ActorSystem) extends AkkaHttpClient(host = host, port = port) {
    val logger = LoggerFactory.getLogger(classOf[JsonDataPointClient])

    def putDataPoint(dataPoint: DataPoint): Future[HttpResponse] = {
//        logger trace "---------------------------------------- Begin DataPoint ----------------------------------------------------------"
//        logger trace (dataPoint.asJson.spaces4)
//        logger trace "---------------------------------------- End DataPoint ------------------------------------------------------------"
        sendAndReceiveAs[DataPoint](Post(s"/api/put", dataPoint))
    }
}
