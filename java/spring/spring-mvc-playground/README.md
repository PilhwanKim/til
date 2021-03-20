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

## 요청 매핑

- 특정 파라미터 조건 매핑
- 특정 헤더 조건 매핑
- 미디어 타입 조건 매핑
    - HTTP 요청 Content-Type, consume
        - HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑
        - 맞지 않으면 HTTP 415 상태코드(Unsupported Media Type)을 반환
    - HTTP 요청 Accept, produce
        - HTTP 요청의 Accept 헤더를 기반으로 미디어 타입으로 매핑
        - 맞지 않으면 HTTP 406 상태코드(Not Acceptable)을 반환

## HTTP 요청 - 기본, 헤더 조회

### MultiValueMap
    - MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
    - HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
    - 예) keyA=value1&keyA=value2

```java
MultiValueMap<String, String> map = new LinkedMultiValueMap();
map.add("keyA", "value1");
map.add("keyA", "value2");
//[value1,value2]
List<String> values = map.get("keyA");
```

### 참고

- @Controller
    - 파라미터 목록 : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annarguments 
    - 응답 값 목록 : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types


## HTTP 요청 파라미터(request parameter)

### GET 의 쿼리 파라메터 전송

```http request
http://localhost:8080/request-param?uesrname=hello&age=20
```

### POST 의 HTML Form 전송

```http request
POST /request-param
Content-Type: application/x-www-form-urlencoded

uesrname=hello&age=20
```

### @RequestParam

- @RequestParam 의 `name(value)` 속성이 파라미터 이름으로 사용
- HTTP 파라미터 이름이 변수 이름과 같으면 `@RequestParam(name="xx")` 생략 가능 (이 방식 추천)
- String , int , Integer 등의 단순 타입이면 @RequestParam 도 *생략 가능*
- 파라메터 필수 여부 (`@RequestParam.required`)
    - 기본값은 true
    - 주의! - 파라미터 이름만 사용 
      - /request-param?username=
      - 파라미터 이름만 있고 값이 없는 경우 빈문자로 통과
    - 주의! - 기본형(primitive)에 null 입력
      - /request-param 요청
      - `@RequestParam(required = false) int age`
- default value
- 파라메터를 Map으로 받기
  - @RequestParam Map ,
    - Map(key=value)
  - @RequestParam MultiValueMap
    - MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1, id2])

### @ModelAttribute

- 스프링MVC는 @ModelAttribute 가 있으면 다음을 실행
  - HelloData 객체를 생성
  - 요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩)
  - 예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력
- 바인딩 오류: 파라메터에서 type 이 맞지 않으면 `BindException` 이 발생

### 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
- String , int , Integer 같은 단순 타입 = @RequestParam
- 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
