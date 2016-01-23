# html2pdf
[![Build Status](https://travis-ci.org/m-doc/html2pdf.svg?branch=master)](https://travis-ci.org/m-doc/html2pdf)
[![codecov.io](https://codecov.io/github/m-doc/html2pdf/coverage.svg?branch=master)](https://codecov.io/github/m-doc/html2pdf?branch=master)
[![Codacy Badge](https://www.codacy.com/project/badge/f6e5369821064b7d96e0bda990e480ad)](https://www.codacy.com/app/fthomas/html2pdf-ms)
[![Join the chat at https://gitter.im/m-doc/html2pdf](https://badges.gitter.im/m-doc/html2pdf.svg)](https://gitter.im/m-doc/html2pdf?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

**html2pdf** is a purely functional microservice for converting HTML to PDF.
It is built on top of [wkhtmltopdf][wkhtmltopdf] for the conversion to PDF and
of [http4s][http4s] and [scalaz-stream][scalaz-stream] for serving them over
HTTP. Here are some live examples of webpages converted to PDF:

* http://html2pdf.timepit.eu/pdf/wikipedia.pdf?url=http://en.wikipedia.org
* http://html2pdf.timepit.eu/pdf/google.pdf?url=http://google.com
* http://html2pdf.timepit.eu/pdf/reddit.pdf?url=http://reddit.com

[http4s]: http://http4s.org
[scalaz-stream]: https://github.com/scalaz/scalaz-stream
[wkhtmltopdf]: http://wkhtmltopdf.org
