
https://11st-tech.github.io/2024/01/04/completablefuture/

- KafkaCompletableFuture
- 참고사항
  - spring kafka 에서 제공하는 KafkaFuture 추상 클래스도 존재하나, CompletableFuture 가 제공하는 기능이 더 많음
  - 


mointor lock 

lock 

asyncio vs threading

https://stackoverflow.com/questions/1312259/what-is-the-re-entrant-lock-and-concept-in-general


monitor
https://bestugi.tistory.com/40


## 자바에서 비동기 사용 방법
1. Thread 클래스 사용 
```
public class AsyncExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            System.out.println("비동기 작업 수행 중...");
            try {
                Thread.sleep(2000);  // 2초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("비동기 작업 완료");
        });

        thread.start();
        System.out.println("메인 스레드 실행 중...");
    }
}
```
2. ExecutorService (스레드 풀)
```
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncExecutorExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(() -> {
            System.out.println("비동기 작업 수행 중...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("비동기 작업 완료");
        });

        executor.shutdown();
    }
}

```
3. CompletableFuture (Java 8 이상)
```
import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    public static void main(String[] args) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("비동기 작업 수행 중...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("비동기 작업 완료");
        });

        future.thenRun(() -> System.out.println("후속 작업 실행"));
        
        System.out.println("메인 스레드 실행 중...");
        future.join();  // 비동기 작업이 끝날 때까지 대기
    }
}

```
4. Reactive Streams (Reactor, RxJava)
```
import reactor.core.publisher.Mono;

public class ReactiveExample {
    public static void main(String[] args) {
        Mono.fromCallable(() -> {
            System.out.println("비동기 작업 수행 중...");
            Thread.sleep(2000);
            return "결과값";
        })
        .doOnNext(System.out::println)
        .subscribe();

        System.out.println("메인 스레드 실행 중...");
    }
}

```
5. Spring 비동기 처리 (@Async)
```
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {
    
    @Async
    public void asyncMethod() {
        System.out.println("비동기 메서드 실행 중...");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("비동기 메서드 완료");
    }
}

```

### 어떤 방법을 선택할지 기준
- 단순한 비동기 작업: Thread 또는 ExecutorService
- 콜백이 많다면: CompletableFuture
- 데이터 스트림 기반: Reactor, RxJava
- Spring 프로젝트: @Async