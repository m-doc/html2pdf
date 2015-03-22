package html2pdf

sealed trait LogEntry[+A] {
  def entry: A
}

case class LogInfo[A](entry: A) extends LogEntry[A]
case class LogDebug[A](entry: A) extends LogEntry[A]
case class LogWarn[A](entry: A) extends LogEntry[A]
case class LogError[A](entry: A) extends LogEntry[A]
