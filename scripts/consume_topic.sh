#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties
TOPIC_NAME=$1

echo "reading Kafka Topic"
echo "Kafka Home: $KAFAKA_HOME"
echo "Kafka Properties: $KAFAKA_PROPERTIES"

cd $KAFAKA_HOME

bin/kafka-console-consumer.sh --topic $TOPIC_NAME \
  --from-beginning \
  --bootstrap-server localhost:9092
