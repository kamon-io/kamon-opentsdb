import sbt.Keys.scalaVersion

name := "kamon-opentsdb"

lazy val root = (project in file(".")).
  aggregate(kamonOpenTSDB, kamonOpenTSDB_HTTP, common).
  settings(
      inThisBuild(Seq(
          crossScalaVersions := Seq("2.12.2", "2.11.8", "2.10.6"),
          scalaVersion := crossScalaVersions.value.head
      ))
  )

lazy val common = Project(id = "common-kamon-open-tsdb-http", base = file("common-kamon-open-tsdb-http")).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.logback,
          CommonDeps.logging.value,
          CommonDeps.configTypesafe,
          CommonDeps.scalaTest % Test
      )
  )

lazy val kamonOpenTSDB = Project(id = "kamon-open-tsdb", base = file("kamon-open-tsdb")).
  dependsOn(common).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.opentsdb,
          CommonDeps.kamon,
          CommonDeps.hbase
      )
  )

lazy val kamonOpenTSDB_HTTP = Project(id = "kamon-open-tsdb-http", base = file("kamon-open-tsdb-http")).
  dependsOn(kamonOpenTSDB).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.akkaHttp.value,
          CommonDeps.circeCore,
          CommonDeps.circeGeneric,
          CommonDeps.circeParser,
          CommonDeps.enumeratum,
          CommonDeps.scalaTest % Test
      )
  )

resolvers += Resolver.bintrayRepo("kamon-io", "releases")
