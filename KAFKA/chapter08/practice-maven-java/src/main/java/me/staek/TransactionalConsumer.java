package me.staek;
import org.apache.kafka.clients.consumer.*;
import java.time.Duration;
import java.util.*;

/**
 * 로컬 카프가 토픽 (topic1", "topic2) 데이터 수신
 */
public class TransactionalConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-new-group-id");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-" + System.currentTimeMillis());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");  // 추가된 설정
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList("topic1", "topic2"));
            System.out.println("Consumer started and waiting for messages...");

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                if (!records.isEmpty()) {
                    System.out.println("Received " + records.count() + " records");

                    for (ConsumerRecord<String, String> record : records) {
                        System.out.printf("Received record: topic = %s, partition = %d, offset = %d, key = %s, value = %s%n",
                                record.topic(), record.partition(), record.offset(), record.key(), record.value());
                    }

                    consumer.commitSync();
                    System.out.println("Committed offsets");
                }
            }
        }
    }
}
