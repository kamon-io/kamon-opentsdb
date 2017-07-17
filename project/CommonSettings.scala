import sbt.Setting

object CommonSettings {
    object settingValues {
        val scalacOptions = Seq(
            "-feature",
            "-language:higherKinds",
            "-language:implicitConversions",
            "-language:existentials",
            "-language:postfixOps",
            "-deprecation",
            "-unchecked")
    }
}
