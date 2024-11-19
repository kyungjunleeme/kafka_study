#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties

# Kafka 종료 함수 정의
shutdown_kafka() {
  echo "Shutting down Kafka Broker"

  # Kafka 종료 시도
  $KAFAKA_HOME/bin/kafka-server-stop.sh

  # Kafka 종료 상태 확인
  if [ $? -ne 0 ]; then
    echo "=================================="
    echo " [ERROR] Kafka Broker failed to stop."
    echo "=================================="
    exit 1
  fi

  echo "Kafka Broker stopped successfully"
}

shutdown_kafka
