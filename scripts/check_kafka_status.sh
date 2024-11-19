#!/bin/bash

# Set environment variables

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties
ZOOKEEPER_HOME=/home/$USER/.local/zookeeper
ZOOKEEPER_VERSION=3.9.3

# Function to check Kafka status
check_kafka_status() {
  echo "Checking Kafka status..."
  if [ -d "$KAFAKA_HOME" ]; then
    echo "Kafka is installed at $KAFAKA_HOME"

    # Check if Kafka server is running
    ps aux | grep -v grep | grep 'kafka.Kafka' >/dev/null
    if [ $? -eq 0 ]; then
      echo "Kafka server is running."
    else
      echo "Kafka server is not running."
    fi

    # Check Kafka logs for errors
    tail -n 50 $KAFAKA_HOME/logs/server.log | grep ERROR
    if [ $? -eq 0 ]; then
      echo "Kafka has error messages in the log:"
      tail -n 20 $KAFAKA_HOME/logs/server.log
    else
      echo "No error messages found in Kafka logs."
    fi
  else
    echo "Kafka is not installed at $KAFAKA_HOME"
  fi
}

# Main script execution
check_kafka_status
echo ""
#telnet localhost 2181 && stat
