
m2 max 기준  # window 사용자는 죄송합니다. 알아서.....ㅜ
https://github.com/confluentinc/cp-all-in-one


```shell
export CONFLUENT_VERSION=7.7.1
echo $CONFLUENT_VERSION

# up
docker compose up -d

# down
docker compose down
```

### 
https://docs.confluent.io/platform/current/installation/docker/config-reference.html

https://github.com/tchiotludo/akhq
진헌님이 알려주셔서 더 적절할 것 같아서 조금 변형함

/Users/kyungjunlee/git_project/personal/kafka_study/confluent_community_zookeeper_kafka
- 아직 테스트 중입니다.
- java 개발자분 gradle 지원 부탁드립니다.

cf) https://devocean.sk.com/blog/techBoardDetail.do?ID=164016

#### 설명 잘되어 있음
https://taaewoo.tistory.com/59

PLAINTEXT_HOST: 사용자 정의 프로토콜 사용시 이름 지정하고 매핑은 시켜줘야함.
https://stackoverflow.com/questions/73849828/what-is-mean-plaintext-host
https://stackoverflow.com/questions/50737950/what-does-plaintext-keyword-means-in-kafka-configuration
https://kafka.apache.org/39/javadoc/org/apache/kafka/common/security/auth/SecurityProtocol.html

https://www.confluent.io/blog/kafka-listeners-explained/


cf)  KafkaConfig.scala 정리
https://chatgpt.com/share/6746bec9-4498-8009-bc74-dd910f436c5c

cf) 공식문서
https://kafka.apache.org/documentation/#brokerconfigs_log.dir

cf) akhq 보충
https://www.instaclustr.com/blog/how-to-use-akhq-with-instaclustr-for-apache-kafka/

cf) to live stream a container's runtime metrics.
https://docs.docker.com/engine/containers/runmetrics/#docker-stats

cf) Kafka Connect docker image
https://devidea.tistory.com/120