#!/bin/sh

sleep 40s # give Bintray time to update the repository

chmod 600 cloud-identity
ssh -q -o StrictHostKeyChecking=no -i cloud-identity $CLOUD_HOST "\
  sudo DEBIAN_FRONTEND=noninteractive apt-get-update-only -y --force-yes m-doc && \
  sudo DEBIAN_FRONTEND=noninteractive apt-get -y --force-yes install html2pdf"
