#!/bin/sh

cd /home/ubuntu/html2pdf-src
sbt clean debian:packageBin
dpkg --unpack target/html2pdf*.deb
apt-get -fy install
