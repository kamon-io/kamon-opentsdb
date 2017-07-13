import sbt.{Def, _}

object PluginDeps {
    object versions {
        val sbtNativePackagerVersion = "1.2.0-M8"
    }

    val sbtNativePackager = addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % versions.sbtNativePackagerVersion)
}

object CommonDeps {
    object versions {

        val akkaVersion = "2.5.3"
        val akkaHttpVersion = "10.0.9"

        val scalaTestVersion = "3.0.1"
        val loggingVersion = "3.5.0"
        val logbackVersion = "1.2.3"
        val configTypesafeVersion = "1.3.1"
        val enumeratumVersion = "1.5.12"
        val mokitoVersion = "1.10.19"
        val circeVersion = "0.8.0"
        val kamonopenTSDBVersion = "0.6.7"
        val kamonVersion = "0.6.7"
        val opentsdbVersion = "2.3.0"
        val hbaseVersion = "1.8.0"
    }

    val akkaHttp = "com.typesafe.akka" %% "akka-http" % versions.akkaHttpVersion

    val logging = "com.typesafe.scala-logging" %% "scala-logging" % versions.loggingVersion
    val logback = "ch.qos.logback" % "logback-classic" % versions.logbackVersion
    val configTypesafe = "com.typesafe" % "config" % versions.configTypesafeVersion

    val kamon = "io.kamon" %% "kamon-core" % versions.kamonVersion
    val opentsdb =  "net.opentsdb" % "opentsdb" % versions.opentsdbVersion excludeAll(
      ExclusionRule(organization = "ch.qos.logback"),
      ExclusionRule(organization = "com.google.gwt"),
      ExclusionRule(organization = "net.opentsdb", artifact = "opentsdb_gwt_theme"),
      ExclusionRule(organization = "org.jgrapht"),
      ExclusionRule(organization = "ch.qos.logback")
    )

    val hbase = "org.hbase" % "asynchbase" % versions.hbaseVersion

    val enumeratum = "com.beachape" %% "enumeratum" % versions.enumeratumVersion

    val circeCore = "io.circe" %% "circe-core" % versions.circeVersion
    val circeParser = "io.circe" %% "circe-parser" % versions.circeVersion
    val circeGeneric = "io.circe" %% "circe-generic" % versions.circeVersion

    val scalaTest = "org.scalatest" %% "scalatest" % versions.scalaTestVersion
    val mokito = "org.mockito" % "mockito-all" % versions.mokitoVersion
}
