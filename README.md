# html2pdf-ms

[![Join the chat at https://gitter.im/fthomas/html2pdf-ms](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/fthomas/html2pdf-ms?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](https://travis-ci.org/fthomas/html2pdf-ms.svg?branch=master)](https://travis-ci.org/fthomas/html2pdf-ms)

**html2pdf-ms** is a microservice for converting HTML to PDF. It is built on top of
[wkhtmltopdf](http://wkhtmltopdf.org) for the conversion from PDF and by [http4s](http://http4s.org)
and [scalaz-stream](https://github.com/scalaz/scalaz-stream) for serving them over HTTP.
Here are some live examples of webpages converted to PDF:

* http://html2pdf.timepit.eu/pdf?url=http://en.wikipedia.org
* http://html2pdf.timepit.eu/pdf?url=http://google.com
* http://html2pdf.timepit.eu/pdf?url=http://reddit.com
