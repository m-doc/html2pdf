#!/bin/sh

ssh -i cloud-identity $CLOUD_HOST \
  apt-get -y --force-yes update && \
  apt-get -y --force-yes install html2pdf
