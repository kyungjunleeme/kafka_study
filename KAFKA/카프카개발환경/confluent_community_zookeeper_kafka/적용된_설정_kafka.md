
### kafka 컨테이너에서 확인

```shell
inter.broker.listener.name=PLAINTEXT
transaction.state.log.min.isr=1
advertised.listeners=PLAINTEXT://kafka-3:9092,PLAINTEXT_HOST://localhost:49092
listener.security.protocol.map=PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
broker.id=3
transaction.state.log.replication.factor=1
listeners=PLAINTEXT://0.0.0.0:9092,PLAINTEXT_HOST://0.0.0.0:49092
zookeeper.connect=zookeeper-1:2181,zookeeper-2:2181,zookeeper-3:2181
log.dirs=/var/lib/kafka/data
```