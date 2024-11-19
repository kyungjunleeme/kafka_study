#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties

TOPIC_NAME=$1

echo "write kafka topic"
echo "Kafka Home: $KAFAKA_HOME"
echo "Kafka Properties: $KAFAKA_PROPERTIES"

cd $KAFAKA_HOME

bin/kafka-console-producer.sh --bootstrap-server localhost:9092 \
  localhost:9092 --topic $TOPIC_NAME
