enablePlugins(MdocPlugin)

name := "html2pdf"
description := "microservice for converting HTML to PDF"

libraryDependencies ++= Seq(
  MdocLibrary.fshell,
  MdocLibrary.renderingEngines,
  Library.http4sBlazeServer,
  Library.http4sCore,
  Library.http4sDsl,
  Library.logbackClassic,
  Library.properly,
  Library.scalaLogging,
  Library.scalacheck % "test"
)

// sbt-native-packager
enablePlugins(JavaServerAppPackaging, DebianPlugin)
debianPackageDependencies in Debian ++= Seq("wkhtmltopdf", "xvfb")
serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV

mdocValidateCommands += "debian:packageBin"
