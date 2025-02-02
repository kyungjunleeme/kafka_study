#!/bin/bash
source ./common_env.sh

echo "Starting Kafka in KRaft mode..."
$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_HOME/config/kraft/server.properties
sleep 5 # Kafka 시작 대기

# Kafka 상태 확인
$KAFKA_HOME/bin/kafka-topics.sh --list --bootstrap-server localhost:9092
if [ $? -ne 0 ]; then
  echo "Failed to start Kafka!"
  exit 1
fi
