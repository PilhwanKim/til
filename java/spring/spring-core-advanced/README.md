# 스프링 핵심 원리 - 고급편


## 로그 추적기 - 요구사항 분석

### 요구사항

- 모든 PUBLIC 메서드의 호출과 응답 정보를 로그로 출력 
- 애플리케이션의 흐름을 변경하면 안됨
  - 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안됨 
- 메서드 호출에 걸린 시간 
- 정상 흐름과 예외 흐름 구분
  - 예외 발생시 예외 정보가 남아야 함
- 메서드 호출의 깊이 표현 
- HTTP 요청을 구분 
  - HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
  - 트랜잭션 ID (DB 트랜잭션X), 여기서는 하나의 HTTP 요청이 시작해서 끝날 때 까지를 하나의 트랜잭션이라 함

### 예시 

```java

정상 요청
[796bccd9] OrderController.request()
[796bccd9] |-->OrderService.orderItem()
[796bccd9] |   |-->OrderRepository.save()
[796bccd9] |   |<--OrderRepository.save() time=1004ms
[796bccd9] |<--OrderService.orderItem() time=1014ms
[796bccd9] OrderController.request() time=1016ms

예외 발생
[b7119f27] OrderController.request()
[b7119f27] |-->OrderService.orderItem()
[b7119f27] | |-->OrderRepository.save() 
[b7119f27] | |<X-OrderRepository.save() time=0ms ex=java.lang.IllegalStateException: 예외 발생! 
[b7119f27] |<X-OrderService.orderItem() time=10ms ex=java.lang.IllegalStateException: 예외 발생! 
[b7119f27] OrderController.request() time=11ms ex=java.lang.IllegalStateException: 예외 발생!
```

## 로그 추적기 V1 - 프로토타입 개발

### 관련 소스

- src
  - trace.TraceId
  - trace.TraceStatus
  - trace.hellotrace.HelloTraceV1
- test
  - trace.hellotrace.HelloTraceV1Test

## 로그 추적기 V1 - 적용

### 관련 소스

- src
  - app.v1.OrderControllerV1dev.leonkim.springcoreadvanced.app.v1.OrderControllerV1
  - app.v1.OrderServiceV1
  - app.v1.OrderRepositoryV1


### 정상 실행 로그

```
[c41539c3] OrderController.request()
[7f805f4e] OrderService.orderItem()
[98a03000] OrderRepository.save()
[98a03000] OrderRepository.save() time=1003ms
[7f805f4e] OrderService.orderItem() time=1004ms
[c41539c3] OrderController.request() time=1005ms
```

![실행 플로우](./img/log-trace-v1.png)

### 예외 실행 로그

```
[27c333f6] OrderController.request()
[62d40a7f] OrderService.orderItem()
[5fbb6590] OrderRepository.save()
[5fbb6590] OrderRepository.save() time=1ms, ex=java.lang.IllegalStateException: 예외 발생!
[62d40a7f] OrderService.orderItem() time=1ms, ex=java.lang.IllegalStateException: 예외 발생!
[27c333f6] OrderController.request() time=1ms, ex=java.lang.IllegalStateException: 예외 발생!
```

로그를 남기기 위한 코드가 생각보다 복잡하다. 
일단은 요구사항을 맞추는 것에 집중한다.

### 남은 요구사항

- 메서드 호출의 깊이 표현
- HTTP 요청을 구분
  - HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분이 가능해야 함
  - 트랜잭션 ID (DB 트랜잭션X)
  