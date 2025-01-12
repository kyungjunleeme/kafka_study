package me.staek;

import eu.rekawek.toxiproxy.Proxy;
import eu.rekawek.toxiproxy.ToxiproxyClient;
import eu.rekawek.toxiproxy.model.Toxic;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class NetworkFailureTest {
    private ToxiproxyClient toxiproxy;
    private Proxy proxy;

    @Before
    public void setUp() throws Exception {
        // Toxiproxy 클라이언트 설정
        toxiproxy = new ToxiproxyClient("localhost", 8474);
        proxy = toxiproxy.getProxy("kafka-proxy");
        System.out.println(proxy);
    }

    public void tearDown() throws Exception {
        // 테스트 후 프록시 연결 정리
        if (proxy != null) {
            // 모든 toxic 제거
            for (Toxic toxic : proxy.toxics().getAll()) {
                toxic.remove();
            }
            // 프록시 삭제
            proxy.delete();
        }
    }
    /**
     *  toxiproxy 통해서 데이터송신 성공
     * @throws Exception
     */
//    @Test
    public void testNormalCase() throws Exception {
        Properties props = new Properties();
        // 프록시 주소로 설정
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "10000");  // 요청 타임아웃을 10초로 설정
//        props.put(ProducerConfig.SOCKET_TIMEOUT_MS_CONFIG, "10000");  // 소켓 타임아웃을 10초로 설정

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:8475");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-transaction-id");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        props.put(ProducerConfig.RETRIES_CONFIG, "3");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "200");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();

            // 메시지 전송 테스트
            producer.beginTransaction();
            for (int i = 0; i < 5; i++) {
                producer.send(new ProducerRecord<>("topic1", "key" + i, "value" + i))
                        .get(5, TimeUnit.SECONDS);
                System.out.println("Sent message " + i);
            }

            producer.commitTransaction();
        }
    }

    /**
     * 네트워크지연으로 인한 timeout 테스트
     *
     * 성공 위해서는
     *              latencyToxic = proxy.toxics()
     *                     .latency("latency-toxic", ToxicDirection.UPSTREAM, 0)
     *                     .setLatency(30000)
     *                     .setJitter(100);
     *                      주석처리 및
     *.get(200, TimeUnit.MILLISECONDS); => 2초로 변경
     *
     * @throws Exception
     */
    @Test
    public void testNetworkFailure() throws Exception {
        Properties props = new Properties();
        // 프록시 주소로 설정
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:8475");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test-transaction-id");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");

        /**
         * 2. 타임아웃 설정의 적절성
         * 설정된 재시도 횟수와 간격이 네트워크 장애 상황에서 어떻게 동작하는지
         * 일시적인 네트워크 문제를 얼마나 잘 극복하는지
         */
        /**
         * 1. 재시도 정책의 효과
         * 실제 운영 환경에서 필요한 적절한 타임아웃 값을 결정하는데 도움
         * 너무 짧은 타임아웃은 불필요한 재시도를, 너무 긴 타임아웃은 리소스 낭비를 초래할 수 있음
         */
        /**
         * 3. 에러 처리 메커니즘
         * 네트워크 장애 시 발생하는 구체적인 예외 타입들 확인
         * 예외 상황에서의 로깅과 모니터링 포인트 파악
         * 실제 운영 환경에서 필요한 에러 처리 로직 설계에 도움
         */

        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1"); // 동시 요청 제한

        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "50");   // 0.1초
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, "50");  // 0.1초
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, "35000");              // 메타데이터/버퍼 대기 시간
        props.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "35000");       // 트랜잭션 타임아웃
        //        props.put("transaction.timeout.ms", "100"); // TRANSACTION_TIMEOUT_CONFIG 와 같은 옵션임


        props.put(ProducerConfig.RETRIES_CONFIG, "1");
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "100");   // 5초 대기

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            producer.initTransactions();

            try {
                // 지연 추가
                /**
                 * 네트워크 지연 상황 테스트 (Latency Toxic)
                 * 1초의 기본 지연과 ±500ms의 변동을 주어 불안정한 네트워크 상황 시뮬레이션
                 * 이 상황에서도 메시지가 정상적으로 전송되는지 확인
                 * 실제 운영 환경의 네트워크 지연/불안정 상황을 시뮬레이션
                 */
                Toxic latencyToxic = null;
                try {

                    producer.beginTransaction();


                    /**
                     * DOWNSTREAM: 카프카 -> 클라이언트 방향의 통신
                     * UPSTREAM: 클라이언트 -> 카프카 방향의 통신
                     */
                    latencyToxic = proxy.toxics()
                            .latency("latency-toxic", ToxicDirection.UPSTREAM, 0)
                            .setLatency(30000)     // 6초 지연
                            .setJitter(100);
                    // ACK
                    Toxic atencyToxic2 = proxy.toxics()
                            .latency("latency-toxic2", ToxicDirection.DOWNSTREAM, 0)
                            .setLatency(30000)     // 6초 지연
                            .setJitter(100);

                    // 첫 번째 메시지 전송 테스트
                    System.out.println("begin");

                    for (int i = 0; i < 5; i++) {
                        producer.send(new ProducerRecord<>("topic1", "key" + i, "value" + i))
//                                .get(2, TimeUnit.SECONDS); // 2초 timeout으로 get
                        .get(200, TimeUnit.MILLISECONDS);
                        System.out.println("Sent message " + i);
                    }
                    producer.commitTransaction();  // 첫 번째 트랜잭션 커밋
                    // 지연 toxic 제거
//                } catch (ExecutionException | TimeoutException e) {
                } catch (Exception e) {
                    System.out.println("Expected failure occurred: " + e);
                    producer.abortTransaction();
                    throw e;
                } finally {
                    latencyToxic.remove();

                }
            } finally {
                // 모든 toxic 제거 확인
                for (Toxic toxic : proxy.toxics().getAll()) {
                    toxic.remove();
                }

            }
        }
    }
}
