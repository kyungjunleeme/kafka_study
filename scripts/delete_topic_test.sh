#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties
TARGET_TOPIC_NAME=$1

echo "delete Kafka Topic"

echo "Kafka Home: $KAFAKA_HOME"
echo "Kafka Properties: $KAFAKA_PROPERTIES"

cd $KAFAKA_HOME

bin/kafka-topics.sh --delete --topic $TARGET_TOPIC_NAME \
  --bootstrap-server localhost:9092

# ensure that the topic is deleted

bin/kafka-topics.sh --list --bootstrap-server localhost:9092 \
  __consumer_offsets
