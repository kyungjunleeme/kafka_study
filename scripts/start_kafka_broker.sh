#!/bin/bash

KAFKA_HOME=/home/$USER/.local/kafka
KAFKA_PROPERTIES=$KAFKA_HOME/config/server.properties

echo "=================================="
echo "KAFKA_HOME: $KAFKA_HOME"
echo "KAFKA_PROPERTIES: $KAFKA_PROPERTIES"
echo "=================================="

echo "Starting Kafka Broker"

# Error Handling
error_handling() {
  echo "=================================="
  echo " [ERROR] Kafka Broker failed to start."
  echo "=================================="
  exit 1
}

# Check Path
if [ ! -d "$KAFKA_HOME" ]; then
  echo "=================================="
  echo " [ERROR] Kafka Home directory not found: $KAFKA_HOME"
  echo "=================================="
  error_handling
fi

if [ ! -f "$KAFKA_PROPERTIES" ]; then
  echo "=================================="
  echo " [ERROR] Kafka Properties file not found: $KAFKA_PROPERTIES"
  echo "=================================="
  error_handling
fi

echo "Kafka Home: $KAFKA_HOME"
echo "Kafka Properties: $KAFKA_PROPERTIES"

# Kafka Broker 시작 시도
$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_PROPERTIES

# Kafka 시작 상태 확인
if [ $? -ne 0 ]; then
  error_handling
fi

echo "Kafka Broker Started successfully"
