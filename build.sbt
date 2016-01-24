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

val http4sVersion = "0.12.0"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion
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

addCommandAlias("validate", ";coverage;test;debian:packageBin;rpm:packageBin")
