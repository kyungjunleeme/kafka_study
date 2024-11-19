#!bin/bash

ZOOKEEPER_HOME=/home/$USER/.local/zookeeper
ZOOKEEPER_VERSION=3.9.3

cd $ZOOKEEPER_HOME/apache-zookeeper-$ZOOKEEPER_VERSION-bin/bin

zkServer.sh stop
