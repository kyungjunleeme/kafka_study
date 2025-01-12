package me.staek;

import org.apache.kafka.clients.producer.*;
import java.util.Properties;

/**
 * 로컬 카프가 토픽 (topic1, topic2) 생성
 */
public class TransactionalProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 트랜잭션을 위한 설정
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();

            try {
                producer.beginTransaction();

                // 여러 메시지 전송
                producer.send(new ProducerRecord<>("topic1", "key1", "value1"));
                producer.send(new ProducerRecord<>("topic2", "key2", "value2"));

                // 트랜잭션 커밋
                producer.commitTransaction();
            } catch (Exception e) {
                // 에러 발생시 롤백
                producer.abortTransaction();
                throw e;
            }
        }
    }
}
