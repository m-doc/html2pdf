#!/bin/sh

update_repo() {
  sudo apt-get -y --force-yes update \
    -o Dir::Etc::sourcelist="sources.list.d/$1.list" \
    -o Dir::Etc::sourceparts="-" \
    -o APT::Get::List-Cleanup="0"
}

ssh -o StrictHostKeyChecking=no -i cloud-identity $CLOUD_HOST \
  update_repo m-doc && \
  sudo apt-get -y --force-yes install html2pdf
