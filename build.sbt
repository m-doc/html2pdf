name := "html2pdf-ms"
version := "0.0.1"
description := "microservice for converting HTML to PDF"

organization := "eu.timepit"
homepage := Some(url("https://github.com/fthomas/html2pdf-ms"))
startYear := Some(2015)
licenses += "GPL-3.0" -> url("http://www.gnu.org/licenses/gpl-3.0.html")
scmInfo := Some(ScmInfo(homepage.value.get, "git@github.com:fthomas/html2pdf-ms.git"))

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

val http4sVersion = "0.6.5"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blazeserver" % http4sVersion
)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

val rootPackage = "html2pdf"
initialCommands := s"""
  import $rootPackage._
"""

Revolver.settings
scalariformSettings

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
buildInfoSettings
sourceGenerators in Compile <+= buildInfo
buildInfoKeys := Seq[BuildInfoKey](
  name,
  BuildInfoKey.map(homepage) { case (k, v) => k -> v.get.toString }
)
buildInfoPackage := rootPackage

addCommandAlias("validate", ";coverage;test;debian:packageBin;rpm:packageBin")
