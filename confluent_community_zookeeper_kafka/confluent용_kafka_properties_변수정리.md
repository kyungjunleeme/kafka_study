
https://docs.confluent.io/platform/current/kafka/deployment.html#cp-production-parameters

### 
```markdown
image: confluentinc/cp-kafka:latest
```

```markdown
Kafka in ZooKeeper mode
If you are using ZooKeeper for cluster metadata management, use the following guidelines.

During startup in ZooKeeper mode, Kafka brokers register themselves in ZooKeeper to become a member of the cluster.

In a production environment, multiple brokers are required.

To configure brokers, navigate to the Apache Kafka® properties file (/etc/kafka/server.properties) and customize the following:
```


### /etc/kafka/server.properties에 기반함
```shell
KAFKA_BROKER_ID=0
KAFKA_NUM_NETWORK_THREADS=3
KAFKA_NUM_IO_THREADS=8
KAFKA_SOCKET_SEND_BUFFER_BYTES=102400
KAFKA_SOCKET_RECEIVE_BUFFER_BYTES=102400
KAFKA_SOCKET_REQUEST_MAX_BYTES=104857600
KAFKA_LOG_DIRS=/var/lib/kafka
KAFKA_NUM_PARTITIONS=1
KAFKA_NUM_RECOVERY_THREADS_PER_DATA_DIR=1
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1
KAFKA_LOG_RETENTION_HOURS=168
KAFKA_LOG_RETENTION_CHECK_INTERVAL_MS=300000
KAFKA_ZOOKEEPER_CONNECT=localhost:2181
KAFKA_ZOOKEEPER_CONNECTION_TIMEOUT_MS=18000
KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS=0
```

### confluent properties as docker enviornment variables
```shell
CONFLUENT_METRIC_REPORTERS=io.confluent.metrics.reporter.ConfluentMetricsReporter
CONFLUENT_METRICS_REPORTER_BOOTSTRAP_SERVERS=localhost:9092
CONFLUENT_METRICS_REPORTER_TOPIC_REPLICAS=1
```




