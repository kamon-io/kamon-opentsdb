package ru.mfms.kamon

import kamon.Kamon
import kamon.metric.instrument.Time

object ConnectorRMIMetrics extends Config {
    lazy val env = config.getString("connector.env")

    private def getTemplateTags(host: SysData, customer: String): Map[String, String] = {
        Map("host" → host.hostname.normalize, "containerId" → host.containerId.normalize, "customer" → customer.normalize) ++ (if (env != "") Map("env" → env) else Map())

        //s"hpx.${host.hostname.normalize}.${host.containerId.normalize}.${env}.${customer.normalize}"
    }

    /*1*/ def connectorRequestCount(host: SysData, customer: String) = Kamon.metrics.counter(s"connector.rmi.request.count", getTemplateTags(host, customer))
    /*2*/ def connectorRequestInvalidCount(host: SysData, customer: String) = Kamon.metrics.counter(s"connector.rmi.request.invalid.count", getTemplateTags(host, customer))
    /*3*/ def connectorRequestMessageCount(host: SysData, customer: String) = Kamon.metrics.counter(s"connector.rmi.request.message.count", getTemplateTags(host, customer))
    /*4*/ def connectorRequestMessageProcessingtime(host: SysData, customer: String) = Kamon.metrics.histogram(s"connector.rmi.request.message.processingTime", getTemplateTags(host, customer), Time.Milliseconds)
    /*5*/ def connectorRequestStatusCount(host: SysData, customer: String) = Kamon.metrics.counter(s"connector.rmi.request.status.count", getTemplateTags(host, customer))
    /*6*/ def connectorRequestStatusProcessingtime(host: SysData, customer: String) = Kamon.metrics.histogram(s"connector.rmi.request.status.processingTime", getTemplateTags(host, customer), Time.Milliseconds)
}

