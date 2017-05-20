#!/usr/bin/env bash

set -o nounset -o errexit -o xtrace

BASEDIR=`dirname "$0"`
HOST=$1
export DOMAIN=$2

scp $BASEDIR/docker-compose.yml $HOST

ssh $HOST <<EOF
sudo docker-compose up -d
EOF