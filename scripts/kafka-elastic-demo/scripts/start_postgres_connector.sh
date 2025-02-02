#!/bin/bash
source ./common_env.sh

echo "Starting Postgres Source Connector..."

$KAFKA_HOME/bin/connect-standalone.sh $KAFKA_HOME/config/connect-standalone.properties \
  $KAFKA_HOME/config/custom/postgres-source.properties
