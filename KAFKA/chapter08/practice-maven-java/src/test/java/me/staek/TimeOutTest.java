package me.staek;

import junit.framework.TestCase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TimeoutException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TimeOutTest extends TestCase {
    private Properties props;

    @Before
    public void setUp() {
        props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    }

    @Test
    public void testSuccessTransaction() {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();
            producer.beginTransaction();

            producer.send(new ProducerRecord<>("topic1", "success-key1", "success-value1"));
            producer.send(new ProducerRecord<>("topic2", "success-key2", "success-value2"));

            producer.commitTransaction();
        }
    }

    /**
     * 타임아웃 테스트
     * REQUEST_TIMEOUT_MS_CONFIG : 10
     * MAX_BLOCK_MS_CONFIG : 10
     * "a".repeat(10_000_000);
     *
     * => 송신성공
     * REQUEST_TIMEOUT_MS_CONFIG : 1000
     * MAX_BLOCK_MS_CONFIG : 1000
     * "a".repeat(10_000);
     */
    @Test
    public void testTimeout() {
        Properties props = new Properties();
        // 기본 필수 설정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        // 트랜잭션 설정
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-transactional-id");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        /**
         * 브로커에 대한 단일 요청의 최대 대기 시간
         * 브로커로부터 응답을 기다리는 시간
         * 기본값: 30000 (30초)
         */
        // 타임아웃 설정
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "10");  // 1ms

        /**
         * send() 메소드나 partitionsFor() 메소드가 블록될 수 있는 최대 시간
         * 버퍼가 가득 찼을 때나 메타데이터를 기다릴 때 적용
         * 기본값: 60000 (60초)
         */
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "10");       // 1ms

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();
            producer.beginTransaction();

            // 큰 메시지를 보내서 타임아웃 유도
//            String largeMessage = "a".repeat(100);
            String largeMessage = "a".repeat(10_000_000);  // 10MB
            producer.send(new ProducerRecord<>("topic3", "key1", largeMessage));

            /**
             * // REQUEST_TIMEOUT_MS_CONFIG 예시
             * producer.send(record);  // 브로커 응답 대기 시간
             *
             * // MAX_BLOCK_MS_CONFIG 예시
             * producer.send(record);  // 버퍼 공간 대기 시간
             * producer.partitionsFor("topic");  // 메타데이터 대기 시간
             */

            producer.commitTransaction();
        } catch (TimeoutException e) {
            System.out.println("Timeout occurred as expected");
        }
    }



    /**
     *재시도 정책 테스트 (미구현)
     * @throws InterruptedException
     */
    //@Test
    public void testRetryPolicy() throws InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "100");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();
            producer.beginTransaction();

            // 여러 번의 메시지 전송
            for (int i = 0; i < 10; i++) {
                producer.send(new ProducerRecord<>("topic1", "key" + i, "value" + i));
                if (i == 5) {
                    // 의도적으로 네트워크 지연 발생
                    Thread.sleep(5000);
                }
            }

            producer.commitTransaction();
        }
    }

    /**
     * 네트워크 실패 테스트 (미구현)
     */
    //@Test
    public void testNetworkFailure() {
        // 테스트 전에 tc 명령어로 네트워크 지연/패킷 손실 설정
        // 터미널에서 실행:
        // sudo tc qdisc add dev lo root netem loss 20% delay 100ms

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:8080");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-network-failure-id");
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();
            producer.beginTransaction();

            // 여러 메시지 전송
            for (int i = 0; i < 100; i++) {
                producer.send(new ProducerRecord<>("topic1", "key" + i, "value" + i));
            }

            producer.commitTransaction();
        } finally {
            // 테스트 후 네트워크 설정 복구
            // sudo tc qdisc del dev lo root
        }
    }

    /**
     * 동시성 문제 테스트 (미구현)
     * @throws InterruptedException
     */
    //@Test
    public void testConcurrency() throws InterruptedException {
        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
                    producer.initTransactions();
                    producer.beginTransaction();

                    for (int j = 0; j < 100; j++) {
                        producer.send(new ProducerRecord<>("topic1",
                                "key-" + threadId + "-" + j,
                                "value-" + threadId + "-" + j));
                    }

                    producer.commitTransaction();
                    latch.countDown();
                }
            });
        }

        latch.await(30, TimeUnit.SECONDS);
    }

    /**
     * 브로커 장애 테스트 (미구현)
     * @throws InterruptedException
     * @throws IOException
     */
  //  @Test
    public void testBrokerFailure() throws InterruptedException, IOException {
        // 주의: 실제 브로커를 중지해야 함

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();
            producer.beginTransaction();

            // 메시지 전송 시작
            producer.send(new ProducerRecord<>("topic1", "key1", "value1"));

            // 브로커 중지 명령 실행
            Runtime.getRuntime().exec("brew services stop kafka");
            Thread.sleep(5000);

            // 추가 메시지 전송 시도
            producer.send(new ProducerRecord<>("topic1", "key2", "value2"));

            producer.commitTransaction();
        } finally {
            // 브로커 재시작
            Runtime.getRuntime().exec("brew services start kafka");
        }
    }

}
