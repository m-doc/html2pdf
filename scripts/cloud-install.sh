#!/bin/sh

chmod 600 cloud-identity
ssh -q -o StrictHostKeyChecking=no -i cloud-identity $CLOUD_HOST \
  "DEBIAN_FRONTEND=noninteractive \
  sudo apt-get -y --force-yes update \
    -o Dir::Etc::sourcelist=\"sources.list.d/m-doc.list\" \
    -o Dir::Etc::sourceparts=\"-\" \
    -o APT::Get::List-Cleanup=\"0\" && \
  sudo apt-get -y --force-yes install html2pdf"
