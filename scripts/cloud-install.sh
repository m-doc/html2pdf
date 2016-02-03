#!/bin/sh

update_repo() {
  # http://askubuntu.com/a/65250
  sudo apt-get -y --force-yes update \
    -o Dir::Etc::sourcelist="sources.list.d/$1.list" \
    -o Dir::Etc::sourceparts="-" \
    -o APT::Get::List-Cleanup="0"
}

chmod 600 cloud-identity
ssh -q -o StrictHostKeyChecking=no -i cloud-identity $CLOUD_HOST \
  update_repo m-doc && \
  sudo apt-get -y --force-yes install html2pdf
