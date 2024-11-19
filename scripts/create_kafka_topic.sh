#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties

# 첫 번째 인자를 변수에 저장
TOPIC_NAME=$1

echo "The first argument is: $TOPIC_NAME"
echo "Creating Kafka Topic"
echo "Kafka Home: $KAFAKA_HOME"
echo "Kafka Properties: $KAFAKA_PROPERTIES"

cd $KAFAKA_HOME

bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic $TOPIC_NAME \
  --partitions 1 \
  --replication-factor 1

echo "Kafka Topic $TOPIC_NAME created successfully"
