#!/bin/sh

echo "deb https://dl.bintray.com/sbt/debian /" | tee /etc/apt/sources.list.d/sbt.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
apt-get -y update
apt-get -y install sbt
