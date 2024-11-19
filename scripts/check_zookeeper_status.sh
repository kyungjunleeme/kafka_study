#!/bin/bash

# Variables
ZOOKEEPER_HOST="localhost"
ZOOKEEPER_PORT="2181"
STATUS_COMMAND="srvr"

# Check if ZooKeeper is running
echo $STATUS_COMMAND | nc $ZOOKEEPER_HOST $ZOOKEEPER_PORT >/dev/null 2>&1

# Check the response
if [ $? -eq 0 ]; then
  RESPONSE=$(echo $STATUS_COMMAND | nc $ZOOKEEPER_HOST $ZOOKEEPER_PORT)
  echo -e "ZooKeeper is running and responding with \n \"$RESPONSE\" "
else
  echo -e "ZooKeeper is not running or not reachable on port $ZOOKEEPER_PORT."
fi
