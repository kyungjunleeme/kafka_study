#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties

# Define the first argument as a variable
TOPIC_NAME=$1
cd $KAFAKA_HOME

echo "Describing Kafka Topic"

// Topic 정보 확인
bin/kafka-topics.sh --describe \
  --bootstrap-server localhost:9092 \
  --topic $TOPIC_NAME
