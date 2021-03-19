# 

## 로깅!

### 선언

```java
// 직접 선언
private final Logger log = LoggerFactory.getLogger(getClass());

// 롬복으로 선언
@Slf4j
@RestController
public class LogTestController {
...
```

### 로그레벨 설정

```properties
#전체 로그 레벨 설정(기본 info)
logging.level.root=info

#hello.springmvc 패키지와 그 하위 로그 레벨 설정
logging.level.hello.springmvc=debug
```

```java
// 이렇게 사용하지 말것 - 로그 찍기 전에 먼저 + 연산을 실행해 버린다.(필요 없을때도 늘 실행하는 문제)
log.debug("data="+data)

// 이렇게 사용할 것 - 로그가 필요할 때 메시지를 합친다.
log.debug("data={}", data)
```

### 로그 사용시 장점

- 쓰레드 정보 클래스 이름 같은 부가 정보 출력, 출력 모양 조정
- 로그 레벨에 따라 출력 여부를 조정 가능(개발 서버: debug, 운영: info)
- System.out, 파일, 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 파일을 분할하여 로그 남김.
- 성능도 일반 System.out 보다 좋음(내부 버퍼링, 멀티 쓰레드 등등). 실무에서는 꼭 로그를 사용.

### 참고

- SLF4J - http://www.slf4j.org
- Logback - http://logback.qos.ch
- Spring Boot - https://docs.spring.io/spring-boot/docs/current/reference/html/spring-bootfeatures.html#boot-features-logging
