#!/bin/sh

cd /home/ubuntu/html2pdf-src
sbt debian:packageBin
dpkg --unpack target/html2pdf*.deb
apt-get -fy install
