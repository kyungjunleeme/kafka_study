#!/bin/bash
source ./common_env.sh

echo "Starting Kafka Connect in standalone mode..."
$KAFKA_HOME/bin/connect-standalone.sh -daemon \
  $KAFKA_HOME/config/connect-standalone.properties \
  $KAFKA_HOME/config/custom/postgres-source.properties \
  $KAFKA_HOME/config/custom/elasticsearch-sink.properties

sleep 10 # 커넥터 시작 대기
echo "Check Kafka Connect status with: curl http://localhost:8083/connectors"
