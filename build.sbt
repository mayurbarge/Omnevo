import sbt.Keys.{baseDirectory, libraryDependencies}
import sbt.Test

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / name := """Omnevo"""
ThisBuild /    assembly / logLevel := Level.Debug
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard

  case x => MergeStrategy.first
}

val AkkaVersion = "2.6.18"
val playVersion = "2.8.13"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  //"-Wunused:_",
  "-Wvalue-discard",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)
lazy val Omnevo =
  project
    .in(file("."))
    .settings(
      assembly / mainClass := Some("com.worldofmusic.main.Main")
    )
  .dependsOn(domain, repository, xmlservices, application)
  .aggregate(domain, repository, xmlservices, application)

lazy val domain =
  project
    .in(file("domain"))
    .settings(commonSettings)

lazy val repository =
  project
    .in(file("repository"))
    .enablePlugins(PlayScala)
    .settings(commonSettings)

lazy val xmlservices =
  project
    .in(file("xmlservices"))
    .enablePlugins(PlayScala)
    .dependsOn(domain)
    .settings(commonSettings)

lazy val application =
  project
    .in(file("application"))
    .dependsOn(domain, xmlservices, repository)
    .settings(
      libraryDependencies ++= Seq(guice)
    )
    .settings(commonSettings)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.8.2",
    "org.typelevel" %% "cats-core" % "2.7.0",
    "org.typelevel" %% "cats-effect" % "3.3.5",
    "org.mockito" % "mockito-core" % "4.3.1",
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
  ),
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Compile / scalaSource := baseDirectory.value / "src/main/scala",
  Test / scalaSource := baseDirectory.value / "src/test/scala",
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value
)


