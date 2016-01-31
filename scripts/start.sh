#!/bin/sh

cd /home/ubuntu/html2pdf-src
sbt debian:packageBin
dpkg -i target/html2pdf*.deb
