# WSL에서 카프카 설치하기 💻

## **1. WSL 설치**
WSL 설치 가이드는 [Microsoft 공식 문서](https://learn.microsoft.com/ko-kr/windows/wsl/install)를 참고하세요.


## **2. 작업 디렉토리 생성**
`home/<user>` 경로 아래에 필요한 디렉토리를 생성합니다.

```bash
mkdir -p ~/.local/tmp
cd ~/.local/tmp
```

## **3. Java 설치 및 확인**
Kafka 실행에 필요한 Java(version 17)를 설치합니다.

```bash
sudo apt update && sudo apt install openjdk-17-jdk
java -version
```

## **4. Zookeeper 설치**

**Zookeeper**는 Kafka 실행에 필수적인 구성 요소입니다.

`~/.local/tmp` 경로에서 다음을 실행하세요.

### **Zookeeper 다운로드 및 압축 해제**

```bash
wget https://dlcdn.apache.org/zookeeper/zookeeper-3.9.3/apache-zookeeper-3.9.3-bin.tar.gz
tar -xvzf apache-zookeeper-3.9.3-bin.tar.gz
```

### **디렉토리 이동 및 이름 변경**

```bash
mv apache-zookeeper-3.9.3-bin ~/.local/zookeeper
```

## **5. Kafka 설치**

Kafka를 다운로드하고 설치합니다.

### **Kafka 다운로드 및 압축 해제**

```bash
cd ~/.local/tmp
wget https://dlcdn.apache.org/kafka/3.9.0/kafka_2.12-3.9.0.tgz
tar -xvzf kafka_2.12-3.9.0.tgz
```

### **디렉토리 이동 및 이름 변경**

```bash
mv kafka_2.12-3.9.0 ~/.local/kafka
```

## **6. Zookeeper 실행**

Zookeeper를 실행합니다.

```bash
cd ~/.local/zookeeper/bin
./zkServer.sh start
```

## **7. Kafka Broker 실행 스크립트 작성**

**편리한 실행을 위해 Kafka Broker를 실행하는 스크립트를 작성합니다.**

### 스크립트 내용

스크립트를 `~/.local` 아래에 저장합니다. 

파일명: `start_kafka_broker.sh` (by. 진헌님)

```bash
#!/bin/bash

KAFKA_HOME=/home/$USER/.local/kafka
KAFKA_PROPERTIES=$KAFKA_HOME/config/server.properties

echo "=================================="
echo "KAFKA_HOME: $KAFKA_HOME"
echo "KAFKA_PROPERTIES: $KAFKA_PROPERTIES"
echo "=================================="

echo "Starting Kafka Broker"

# Error Handling
error_handling() {
  echo "=================================="
  echo " [ERROR] Kafka Broker failed to start."
  echo "=================================="
  exit 1
}

# Check Path
if [ ! -d "$KAFKA_HOME" ]; then
  echo "=================================="
  echo " [ERROR] Kafka Home directory not found: $KAFKA_HOME"
  echo "=================================="
  error_handling
fi

if [ ! -f "$KAFKA_PROPERTIES" ]; then
  echo "=================================="
  echo " [ERROR] Kafka Properties file not found: $KAFKA_PROPERTIES"
  echo "=================================="
  error_handling
fi

echo "Kafka Home: $KAFKA_HOME"
echo "Kafka Properties: $KAFKA_PROPERTIES"

# Kafka Broker 시작
$KAFKA_HOME/bin/kafka-server-start.sh -daemon $KAFKA_PROPERTIES

# Kafka 시작 상태 확인
if [ $? -ne 0 ]; then
  error_handling
fi

echo "Kafka Broker Started successfully"

```

### **실행 권한 부여**
```bash
chmod +x ~/.local/start_kafka_broker.sh
```

### **스크립트 실행**
```bash
~/.local/start_kafka_broker.sh
```

## **요약**
- Java 설치 → Zookeeper 설치 및 실행 → Kafka 설치 → 실행 스크립트 작성
- 위 과정에 따라 Kafka 및 Zookeeper를 쉽게 WSL 환경에서 실행할 수 있습니다.


## **추가로 실행해보면 좋은 내용 👍**
토픽생성 스크립트(by 진헌님)

```bash
#!/bin/bash

KAFAKA_HOME=/home/$USER/.local/kafka
KAFAKA_PROPERTIES=$KAFAKA_HOME/config/server.properties

첫 번째 인자를 변수에 저장
TOPIC_NAME=$1

echo "The first argument is: $TOPIC_NAME"
echo "Creating Kafka Topic"
echo "Kafka Home: $KAFAKA_HOME"
echo "Kafka Properties: $KAFAKA_PROPERTIES"

cd $KAFAKA_HOME

bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic $TOPIC_NAME \
  --partitions 1 \
  --replication-factor 1

echo "Kafka Topic $TOPIC_NAME created successfully"
```
