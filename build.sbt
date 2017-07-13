import com.typesafe.sbt.SbtGit.git

name := "kamon-opentsdb"

lazy val root = (project in file(".")).
  enablePlugins(GitVersioning).
  aggregate(kamonOpenTSDB, kamonOpenTSDB_HTTP, common, test).
  settings(
      inThisBuild(Seq(
          git.baseVersion := CommonSettings.settingValues.baseVersion,
          scalaVersion := CommonSettings.settingValues.scalaVersion,
          publishTo := {
              val corporateRepo = "http://toucan.simplesys.lan/"
              if (isSnapshot.value)
                  Some("snapshots" at corporateRepo + "artifactory/libs-snapshot-local")
              else
                  Some("releases" at corporateRepo + "artifactory/libs-release-local")
          },
          credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
      )
        ++ CommonSettings.defaultSettings),
      publishArtifact in(Compile, packageBin) := false,
      publishArtifact in(Compile, packageDoc) := false,
      publishArtifact in(Compile, packageSrc) := false
  )

lazy val common = Project(id = "common-kamon-open-tsdb-http", base = file("common-kamon-open-tsdb-http")).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.logback,
          CommonDeps.logging,
          CommonDeps.configTypesafe,
          CommonDeps.scalaTest % Test
      )
  ).settings(CommonSettings.defaultProjectSettings)

lazy val kamonOpenTSDB = Project(id = "kamon-open-tsdb", base = file("kamon-open-tsdb")).
  dependsOn(common).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.opentsdb,
          CommonDeps.kamon,
          CommonDeps.hbase
      )
  ).settings(CommonSettings.defaultProjectSettings)

lazy val kamonOpenTSDB_HTTP = Project(id = "kamon-open-tsdb-http", base = file("kamon-open-tsdb-http")).
  dependsOn(kamonOpenTSDB).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.akkaHttp,
          CommonDeps.circeCore,
          CommonDeps.circeGeneric,
          CommonDeps.circeParser,
          CommonDeps.enumeratum,
          CommonDeps.scalaTest % Test
      )
  ).settings(CommonSettings.defaultProjectSettings)

lazy val test = (project in file("test")).
  dependsOn(kamonOpenTSDB_HTTP).
  settings(
      libraryDependencies ++= Seq(
          CommonDeps.scalaTest % Test
      )
  ).settings(CommonSettings.defaultProjectSettings)

resolvers += Resolver.bintrayRepo("kamon-io", "releases")
