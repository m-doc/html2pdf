package object html2pdf {
  type LogWriter[F[_], O] = scalaz.stream.Writer[F, logging.LogEntry, O]

  val bufferSize = 8192
}
