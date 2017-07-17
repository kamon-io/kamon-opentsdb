package kamon.opentsdb

import akka.actor._
import akka.event.Logging
import kamon.Kamon
import kamon.util.ConfigTools.Syntax

import scala.collection.JavaConverters._

/*
* OpenTSDB_Http Extension
*/

object OpenTSDB_Http extends ExtensionId[OpenTSDBExtensionHttp] with ExtensionIdProvider {
  override def createExtension(system: ExtendedActorSystem): OpenTSDBExtensionHttp = new OpenTSDBExtensionHttp(system)
  override def lookup(): ExtensionId[_ <: Extension] = OpenTSDB_Http
}

class OpenTSDBExtensionHttp(system: ExtendedActorSystem) extends Kamon.Extension {
  implicit val actorSystem = system

  Logging(system, classOf[OpenTSDBExtensionHttp]).info("Starting the Kamon(OpenTSDB_Http) extension")

  private val openTSDB_HttpConfig = system.settings.config.getConfig("kamon.opentsdb")

  val httpSender = new DataPointSenderHttp(openTSDB_HttpConfig.getString("http.host"), openTSDB_HttpConfig.getInt("http.port"))

  protected val metricsListener = system.actorOf(DataPointGeneratingActor.props(openTSDB_HttpConfig, httpSender), "opentsdb-metrics-generator")

  protected val subscriptions = openTSDB_HttpConfig.getConfig("subscriptions")                                           

  subscriptions.firstLevelKeys.foreach { subscriptionCategory ⇒
    subscriptions.getStringList(subscriptionCategory).asScala.foreach { pattern ⇒
      Kamon.metrics.subscribe(subscriptionCategory, pattern, metricsListener, permanently = true)
    }
  }

  actorSystem.registerOnTermination {
    Logging(actorSystem, classOf[OpenTSDBExtensionHttp]).info("Shutting down TSDB Http sender")
    httpSender.shutdown()
  }

  Logging(system, classOf[OpenTSDBExtensionHttp]).info("Started the Kamon(OpenTSDB_Http) extension")
}
