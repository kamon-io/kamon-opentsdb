import sbt.Setting

object CommonSettings {
    object settingValues {
        val organization = "ru.mfms.mfmd"

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
        //aether.AetherPlugin.autoImport.overridePublishSettings
        Seq()
    }

    val dockerGroupName = "mfmd"
}
