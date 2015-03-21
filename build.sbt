name := "html2pdf-ms"

version := "0.0.0"

scalaVersion := "2.11.6"
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

val http4sVersion = "0.6.4"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blazeserver" % http4sVersion
)

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

initialCommands := """
  import html2pdf._
"""

scalariformSettings

// package settings
enablePlugins(DebianPlugin)
enablePlugins(JavaServerAppPackaging)

maintainer := "Frank S. Thomas <frank@timepit.eu>"
packageSummary := "Purely functional microservice for HTML to PDF conversion"
packageDescription := "TODO"
debianPackageDependencies in Debian ++= Seq("wkhtmltopdf")
serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV
