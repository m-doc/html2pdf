enablePlugins(MdocPlugin)

name := "html2pdf"
description := "microservice for converting HTML to PDF"

resolvers += "fthomas/maven" at "https://dl.bintray.com/fthomas/maven"

libraryDependencies ++= Seq(
  "eu.timepit" %% "properly" % "0.0.0-18-g5fef5a3",
  "org.http4s" %% "http4s-core" % Version.http4s,
  "org.http4s" %% "http4s-dsl" % Version.http4s,
  "org.http4s" %% "http4s-blaze-server" % Version.http4s,
  "org.m-doc" %% "fshell" % "0.0.0-10-g4a61b77",
  "ch.qos.logback" % "logback-classic" % Version.logback,
  "org.scalacheck" %% "scalacheck" % Version.scalacheck % "test"
)

// sbt-native-packager
enablePlugins(JavaServerAppPackaging)
maintainer := "Frank S. Thomas <frank@timepit.eu>"
packageSummary := description.value
packageDescription := s"See <${homepage.value.getOrElse("")}> for more information."

// deb settings
enablePlugins(DebianPlugin)
debianPackageDependencies in Debian ++= Seq("wkhtmltopdf", "xvfb", "logrotate")
serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV

validateCommands += "debian:packageBin"
