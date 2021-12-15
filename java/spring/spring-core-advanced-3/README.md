# 스프링 핵심 원리 - 고급편 3

## 스프링 AOP 구현

> 참고
> 
> `@Aspect` 를 사용하려면 `@EnableAspectJAutoProxy` 를 스프링 설정에 추가해야 하지만, 스프링 부트를 사용하면 자동으로 추가된다.

### 예제 프로젝트 만들기

- `AopTest` 기본 테스트 참고
- `OrderRepository`, `OrderService` 구현 로직 참고

### 구현1 - 시작

- `AspectV1` 에스펙트 구현
- `AopTest` 에 `@Import(AspectV1.class)` 추가

### 스프링 AOP 구현2 - 포인트컷 분리

- `AspectV2` 에스펙트 구현
- `AopTest` 에 `@Import(AspectV2.class)` 추가

### 스프링 AOP 구현3 - 어드바이스 추가

- `AspectV3` 에스펙트 구현
- `AopTest` 에 `@Import(AspectV3.class)` 추가

![img.png](img/aop-apply-flow.png)

- 포인트것이 적용된 AOP 결과는 다음과 같다. 
  - `orderService` : doLog() , doTransaction() 어드바이스 적용 
  - `orderRepository` : doLog() 어드바이스 적용

### 스프링 AOP 구현4 - 포인트컷 참조

- 포인트컷을 공용으로 사용하기 위해 별도의 외부 클래스에 모아두기
- `AspectV4Pointcut` 에스펙트 구현
- `Pointcuts` 포인트컷 모음
- `AopTest` 에 `@Import(AspectV4Pointcut.class)` 추가
