package ru.mfms.mfmd

import com.typesafe.scalalogging.LazyLogging
import kamon.Kamon
import ru.mfms.kamon.ConnectorRMIMetrics.connectorRequestCount
import ru.mfms.kamon.{Config, SysData}

object TestApp extends App with Config with LazyLogging {
    Kamon.start()

    //(0 to 5).foreach(item ⇒
        (0 to 10).foreach(item1 ⇒
            connectorRequestCount(SysData(), s"accnt_$item1").increment()
        )
    //)

    //sys.exit()

    sys.addShutdownHook {
        () ⇒
            logger info s"system terminating"
            Kamon.shutdown()
            //system.terminate()
            logger info s"system terminated"
    }

}
