# html2pdf-ms

[![Build Status](https://travis-ci.org/fthomas/html2pdf-ms.svg?branch=master)](https://travis-ci.org/fthomas/html2pdf-ms)
[![Codacy Badge](https://www.codacy.com/project/badge/f6e5369821064b7d96e0bda990e480ad)](https://www.codacy.com/app/fthomas/html2pdf-ms)

**html2pdf-ms** is a microservice for converting HTML to PDF. It is built on
top of [wkhtmltopdf](http://wkhtmltopdf.org) for the conversion to PDF and of
[http4s](http://http4s.org) and [scalaz-stream](https://github.com/scalaz/scalaz-stream)
for serving them over HTTP. Here are some live examples of webpages converted
to PDF:

* http://html2pdf.timepit.eu/pdf?url=http://en.wikipedia.org
* http://html2pdf.timepit.eu/pdf?url=http://google.com
* http://html2pdf.timepit.eu/pdf?url=http://reddit.com
