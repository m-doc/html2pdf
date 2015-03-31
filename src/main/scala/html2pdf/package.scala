import html2pdf.logging.LogEntry

import scalaz.stream.Writer

package object html2pdf {
  type LogWriter[F[_], O] = Writer[F, LogEntry, O]
}
