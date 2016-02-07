#!/bin/sh

sleep 30s # give Bintray time to update the repository

chmod 600 cloud-identity
ssh -q -o StrictHostKeyChecking=no -i cloud-identity $CLOUD_HOST \
  "export DEBIAN_FRONTEND=noninteractive && \
  sudo apt-get-update-only -y --force-yes m-doc && \
  sudo apt-get -y --force-yes install html2pdf"
