enablePlugins(BuildInfoPlugin)

name := "html2pdf"
version := "0.0.1"
description := "microservice for converting HTML to PDF"

organization := "org.m-doc"
bintrayOrganization := Some("m-doc")
homepage := Some(url("https://github.com/m-doc/html2pdf"))
startYear := Some(2015)
licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
scmInfo := Some(ScmInfo(homepage.value.get, "git@github.com:m-doc/html2pdf.git"))

scalaVersion := "2.11.7"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

resolvers += "Frank's Bintray" at "https://dl.bintray.com/fthomas/maven"
resolvers += "m-doc's Bintray" at "https://dl.bintray.com/m-doc/maven"

val http4sVersion = "0.12.0"
libraryDependencies ++= Seq(
  "eu.timepit" %% "properly" % "0.0.0-18-g5fef5a3",
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.m-doc" %% "fshell" % "0.0.0-7-g3313efb",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test"
)

val rootPackage = "html2pdf"
initialCommands := s"""
  import $rootPackage._
"""

Revolver.settings

// sbt-native-packager
enablePlugins(JavaServerAppPackaging)
maintainer := "Frank S. Thomas <frank@timepit.eu>"
packageSummary := description.value
packageDescription := s"See <${homepage.value.get}> for more information."

// deb settings
enablePlugins(DebianPlugin)
debianPackageDependencies in Debian ++= Seq("wkhtmltopdf", "xvfb", "logrotate")
serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV

// rpm settings
enablePlugins(RpmPlugin)
rpmVendor := organization.value
rpmLicense := licenses.value.headOption.map(_._1)
rpmGroup := Some("System Environment/Daemons")
rpmBrpJavaRepackJars := true

// sbt-buildinfo
buildInfoKeys := Seq[BuildInfoKey](
  name,
  BuildInfoKey.map(homepage) { case (k, v) => k -> v.get.toString }
)
buildInfoPackage := rootPackage

addCommandAlias("validate", Seq(
  "clean",
  "coverage",
  "test",
  "scalastyle",
  "test:scalastyle",
  "doc",
  "debian:packageBin",
  "rpm:packageBin"
).mkString(";", ";", ""))
