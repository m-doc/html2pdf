package org.mdoc.html2pdf.logging

sealed trait LogLevel {
  def toUpperCase: String =
    toString.toUpperCase
}

case object Info extends LogLevel
case object Debug extends LogLevel
case object Warn extends LogLevel
case object Error extends LogLevel
