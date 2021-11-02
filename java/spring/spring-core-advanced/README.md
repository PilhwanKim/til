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
  - app.v1.OrderControllerV1
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


## 로그 추적기 V2 - 파라미터로 동기화 개발

첫 로그에서 사용한 트랜잭션ID 와 level 을 각 layer 에서 다음 로그에 넘겨준다.

![실행 플로우](./img/log-trace-v2.png)

### 관련 소스

- src
  - trace.hellotrace.HelloTraceV2
- test
  - trace.hellotrace.HelloTraceV2Test
  
### beginSync(..)

기존 TraceId 에서 createNextId() 를 통해 다음ID를 구한다. 
- 트랜잭션ID는 기존과 같이 유지한다.
- 깊이를 표현하는 Level은 하나 증가한다. ( 0 -> 1 )

### 정상 실행 로그

```
[5e985874] hello1
[5e985874] |-->hello2
[5e985874] |<--hello2 time=3ms
[5e985874] hello1 time=7ms
```

### 예외 실행 로그

```
[c62f819c] hello1
[c62f819c] |-->hello2
[c62f819c] |<--hello2 time=3ms, ex=java.lang.IllegalStateException
[c62f819c] hello1 time=7ms, ex=java.lang.IllegalStateException
```

같은 트렌잭션 ID를 유지하고 level 을 통해 메서드 호출의 깊이를 표현 가능해졌다.

## 로그 추적기 V2 - 적용

### 관련 소스

- src
  - app.v2.OrderControllerV2
  - app.v2.OrderServiceV2
  - app.v2.OrderRepositoryV2

### 정상 실행 로그

```
[07e5f4df] OrderController.request()
[07e5f4df] |-->OrderService.orderItem()
[07e5f4df] |   |-->OrderRepository.save()
[07e5f4df] |   |<--OrderRepository.save() time=1003ms
[07e5f4df] |<--OrderService.orderItem() time=1004ms
[07e5f4df] OrderController.request() time=1004ms
```

### 예외 실행 로그

```
[55e54f79] OrderController.request()
[55e54f79] |-->OrderService.orderItem()
[55e54f79] |   |-->OrderRepository.save()
[55e54f79] |   |<--OrderRepository.save() time=1ms, ex=java.lang.IllegalStateException: 예외 발생!
[55e54f79] |<--OrderService.orderItem() time=1ms, ex=java.lang.IllegalStateException: 예외 발생!
[55e54f79] OrderController.request() time=1ms, ex=java.lang.IllegalStateException: 예외 발생!
```

모든 요구사항을 만족하지만.. 한가지 문제가 더 남아있다.

