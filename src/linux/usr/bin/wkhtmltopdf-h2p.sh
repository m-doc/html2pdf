#!/bin/sh
xvfb-run -a -s "-screen 0 1280x1024x24" wkhtmltopdf --quiet "$@"
