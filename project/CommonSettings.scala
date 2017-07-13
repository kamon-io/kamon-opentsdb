import sbt.Keys.crossScalaVersions
import sbt.Setting

object CommonSettings {
    object settingValues {
        val baseVersion = "1.0"

        val scalaVersion = "2.12.2"
        val organization = "ru.mfms.mfmd"
        
        //crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.2")

        val scalacOptions = Seq(
            "-feature",
            "-language:higherKinds",
            "-language:implicitConversions",
            "-language:existentials",
            "-language:postfixOps",
            "-deprecation",
            "-unchecked")
    }

    val defaultSettings = {
        import sbt.Keys._
        Seq(
            scalacOptions := settingValues.scalacOptions,
            organization := settingValues.organization
        )
    }

    val defaultProjectSettings: Seq[Setting[_]] = {
        aether.AetherPlugin.autoImport.overridePublishSettings
    }

    val dockerGroupName = "mfmd"
}
