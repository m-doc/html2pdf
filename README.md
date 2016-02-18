html2pdf has been replaced by https://github.com/m-doc/rendering-service

---

# html2pdf
[![Build Status](https://travis-ci.org/m-doc/html2pdf.svg?branch=master)](https://travis-ci.org/m-doc/html2pdf)
[![codecov.io](https://codecov.io/github/m-doc/html2pdf/coverage.svg?branch=master)](https://codecov.io/github/m-doc/html2pdf?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/18f97a2ac67a4d9e852d2a1bd806228f)](https://www.codacy.com/app/fthomas/html2pdf)
[![Join the chat at https://gitter.im/m-doc/general](https://badges.gitter.im/m-doc/general.svg)](https://gitter.im/m-doc/general)
[![Download](https://api.bintray.com/packages/m-doc/debian/html2pdf/images/download.svg)](https://bintray.com/m-doc/debian/html2pdf/_latestVersion)
[![m-doc.org version](https://img.shields.io/badge/m--doc.org-version-blue.svg)](http://html2pdf.m-doc.org/version)

**html2pdf** is a purely functional microservice for converting HTML to PDF.
It is built on top of [wkhtmltopdf][wkhtmltopdf] for the conversion to PDF and
of [http4s][http4s] and [scalaz-stream][scalaz-stream] for serving them over
HTTP. Here are some live examples of webpages converted to PDF:

* http://html2pdf.m-doc.org/pdf/wikipedia.pdf?url=http://en.wikipedia.org
* http://html2pdf.m-doc.org/pdf/google.pdf?url=http://google.com
* http://html2pdf.m-doc.org/pdf/reddit.pdf?url=http://reddit.com

[http4s]: http://http4s.org
[scalaz-stream]: https://github.com/scalaz/scalaz-stream
[wkhtmltopdf]: http://wkhtmltopdf.org
