from confluent_kafka import Producer
import json

# Kafka 브로커 및 토픽 설정
KAFKA_BROKER = "kafka:9092"
JSON_TOPIC = "json"  # JSON 데이터를 보낼 토픽
CSV_TOPIC = "csv"    # CSV 데이터를 보낼 토픽

# Kafka 프로듀서 설정
producer_config = {
    'bootstrap.servers': KAFKA_BROKER,
    'client.id': 'test-data-producer',
}
producer = Producer(producer_config)

# Kafka에 메시지 전송 시 콜백 함수
def delivery_report(err, msg):
    if err is not None:
        print(f"Message delivery failed: {err}")
    else:
        print(f"Message delivered to {msg.topic()} [{msg.partition()}] at offset {msg.offset()}")

# JSON 데이터 생성 및 전송
def send_json_data():
    json_data = [
        {"_id": "5c4b2b45ab234c86955f0802", "index": 0, "guid": "d3637b06-9940-4958-9f82-639001c14c34"},
        {"_id": "5c4b2b459ffa9bb0c0c249e1", "index": 1, "guid": "08612fb5-40a7-45e5-9ff2-beb89a1b2835"},
        {"_id": "5c4b2b4545d7cbc7bf8b6e3e", "index": 2, "guid": "4880280a-cf8b-4884-881e-7b64ebf2afd0"},
        {"_id": "5c4b2b45dab381e6b3024c6d", "index": 3, "guid": "36d04c26-0dae-4a8e-a66e-bde9b3b6a745"},
        {"_id": "5c4b2b45d1103ce30dfe1947", "index": 4, "guid": "14d53f2c-def3-406f-9dfb-c29963fdc37e"},
        {"_id": "5c4b2b45d6d3b5c51d3dacb7", "index": 5, "guid": "a20cfc3a-934a-4b93-9a03-008ec651b5a4"}
    ]

    for record in json_data:
        producer.produce(
            JSON_TOPIC,
            key=str(record["_id"]),
            value=json.dumps(record),
            callback=delivery_report
        )
    producer.flush()
    print(f"Sent {len(json_data)} JSON messages to topic '{JSON_TOPIC}'.")

# CSV 데이터 생성 및 전송
def send_csv_data():
    csv_data = [
        "1,Sauncho,Attfield,sattfield0@netlog.com,Male,221.119.13.246",
        "2,Luci,Harp,lharp1@wufoo.com,Female,161.14.184.150",
        "3,Hanna,McQuillan,hmcquillan2@mozilla.com,Female,214.67.74.80",
        "4,Melba,Lecky,mlecky3@uiuc.edu,Female,158.112.18.189",
        "5,Mordecai,Hurdiss,mhurdiss4@rambler.ru,Male,175.123.45.143"
    ]

    for record in csv_data:
        producer.produce(
            CSV_TOPIC,
            key=record.split(",")[0],  # 첫 번째 열을 key로 사용
            value=record,
            callback=delivery_report
        )
    producer.flush()
    print(f"Sent {len(csv_data)} CSV messages to topic '{CSV_TOPIC}'.")

# 메인 실행 함수
if __name__ == "__main__":
    print("Starting to send test data to Kafka...")
    send_json_data()
    send_csv_data()
    print("All test data sent successfully.")

