# Spring MVC Features

## 설명 

본 프로젝트는 스프링 MVC 각 요소에 대한 예제와 설명이 담긴 프로젝트이다.

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

> 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
> - String , int , Integer 같은 단순 타입 = @RequestParam
> - 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)

## HTTP 요청 메시지

요청 파라미터와 다르게, HTTP *메시지 바디*를 통해 데이터가 직접 데이터가 넘어오는 경우는 `@RequestParam` , `@ModelAttribute` 를 사용할 수 없다.

### 단순 텍스트 요청 메시지

- HttpEntity: HTTP header, body 정보를 편리하게 조회
    - 메시지 바디 정보를 직접 조회
    - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X
- HttpEntity 는 응답에도 사용 가능
    - 메시지 바디 정보 직접 반환
    - 헤더 정보 포함 가능
    - view 조회X
- HttpEntity 상속
    - RequestEntity
        - HttpMethod, url 정보가 추가, 요청에서 사용
    - ResponseEntity 
        - HTTP 상태 코드 설정 가능, 응답에서 사용 
        - return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)

### 요청 파라메터 vs 메시지 바디

| 요청 파라메터                      | 메시지 바디     |     
|--------------------------------|--------------|
| @RequestParam @ModelAttribute | @RequestBody |

### @ResponseBody

- 응답결과를 HTTP 메시지 바디에 직접 담아서 전달 할 수 있음
- view 를 사용하지 않음

### JSON 요청 메시지

#### *@RequestBody* 객체 파라메터

- 직접 만든 객체를 지정할 수 있다.
- `HttpEntity` , `@RequestBody` 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
- HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환해주는데, 우리가 방금 V2에서 했던 작업을 대신 처리해준다.

> 스프링은 @ModelAttribute , @RequestParam 해당 생략시 다음과 같은 규칙을 적용한다.
> - String , int , Integer 같은 단순 타입 = @RequestParam
> - 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)

HelloData에 @RequestBody 를 생략하면 @ModelAttribute 가 적용되어버린다.

> `HelloData data` -> `@ModelAttribute HelloData data`

따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다.

> ### 주의
> 
> HTTP 요청시에 content-type이 application/json 이어야 JSON을 처리할 수 있는 HTTP 메시지 컨버터가 실행된다.

#### 바디 메시지 처리 과정

- @RequestBody 요청
  - JSON 요청 -> HTTP 메시지 컨버터 -> 객체 
- @ResponseBody 응답
  - 객체 -> HTTP 메시지 컨버터 -> JSON 응답

## HTTP 응답

### 응답 데이터 종류

- 정적 리소스(HTML, css, js 파일)
- 뷰 템플릿 사용(동적 HTML - 서버사이드 렌더링)
- HTTP 메시지 사용(HTTP API)

### 정적 리소스

- 프로젝트의 아래 디렉토리의 정적 리소스 제공
  - `/resources`
  - `/static`
  - `/public`
  - `/META-INF/resources`

- 경로
    - 실 경로: resources/static/basic/hello-form.html
    - URL 경로: http://localhost:8080/basic/hello-form.html

### 뷰 템플릿

- 기본 경로: `src/main/resources/templates`
- `dev.leonkim.springmvcplayground.basic.response.ResponseViewController` 예제 확인
- thymeleaf
    - 기본 설정
        - `spring.thymeleaf.prefix=classpath:/templates/`
        - `spring.thymeleaf.suffix=.html`
    - 참고 : https://docs.spring.io/spring-boot/docs/2.4.3/reference/html/appendix-applicationproperties.html#common-application-properties-templating
    
### HTTP API - 메시지 바디에 직접 입력

- HTML(X), 데이터(주로 JSON)를 담는경우
- @RestController =  @Controller + 컨트롤러에 모두 @ResponseBody 가 적용.
- `dev.leonkim.springmvcplayground.basic.response.ResponseBodyController` 예제 확인

## HTTP 메시지 컨버터

- 인터페이스: `org.springframework.http.converter.HttpMessageConverter`
- 응답은 HTTP `Accept 헤더` 와 `컨트롤러 반환 타입` 등을 조합해서 `HttpMessageConverter` 를 선택함

### HTTP 메시지 컨버터 적용 대상

| 요청                            | 응답           |     
|--------------------------------|--------------|
| `@RequestBody` `HttpEntity(RequestEntity)` | `@ResponseBody` `HttpEntity(ResponseEntity)` |

### 스프링 부트 기본 메시지 컨버터

| 순위 | 이름         | 클래스 타입 | 미디어 타입 | 요청 예 | 응답 예|    
|---|--------------|----------|---------|--------|-----|
| 0 | ByteArrayHttpMessageConverter             | byte[]   | `*/*` |`@RequestBody byte[] data`|`@ResponseBody return byte[]`|
| 1 | StringHttpMessageConverter                | String   | `*/*` |`@RequestBody String data`|`@ResponseBody return "ok"`|
| 2 | MappingJackson2HttpMessageConverter       | HashMap or 객체 | application/json |`@RequestBody HelloData data`|`@ResponseBody return helloData`|

### HTTP 요청 데이터 읽기 과정

1. HTTP 요청이 오고, 컨트롤러에서 `@RequestBody` , `HttpEntity` 파라미터를 사용한다.
2. 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 canRead() 를 호출한다.
   1. 대상 클래스 타입을 지원하는가.
       1. 예) `@RequestBody` 의 대상 클래스 ( byte[] , String , HelloData )
   2. HTTP 요청의 Content-Type 미디어 타입을 지원하는가.
       1. 예) `text/plain` , `application/json` , `*/*`
3. canRead() 조건을 만족하면 read() 를 호출해서 객체 생성하고, 반환한다.

### HTTP 응답 데이터 생성 과정

1. 컨트롤러에서 @ResponseBody , HttpEntity 로 값이 반환된다.
2. 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 canWrite() 를 호출한다.
    1. 대상 클래스 타입을 지원하는가.
        1. 예) return의 대상 클래스 ( byte[] , String , HelloData )
    2. HTTP 요청의 Accept 미디어 타입을 지원하는가.(더 정확히는 @RequestMapping 의 produces )
        1. 예) text/plain , application/json , */*
3. canWrite() 조건을 만족하면 write() 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다.

## RequestMappingHandlerAdepter 구조

- `@RequestMapping`이 달린 핸들러들의 모든 메시지 컨버팅은 `RequestMappingHandlerAdepter` 에서 이루어진다.

![동작 방식](img/request-mapping-handler-adepter.png)

### ArgumentResolver

- 컨트롤러(핸들러)가 필요로 하는 다양한 파라미터의 값(객체)을 생성한다.
- 파리미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다.
- `org.springframework.web.method.support.HandlerMethodArgumentResolver`
- 참고 : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annarguments

### ReturnValueHandler

- `ArgumentResolver`와 비슷, 응답 값을 변환하고 처리한다
- `org.springframework.web.method.support.HandlerMethodReturnValueHandler`
- 참고 : https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-annreturn-types

### HttpMessageConverter

- `ArgumentResolver` 와 `ReturnValueHandler` 가 사용
- HTTP 요청
  - `@RequestBody` 혹은 `HttpEntity` 담당 `ArgumentResolver`
  - 위의 친구들이 메시지 컨버터를 호출해 필요 객체를 생성
- HTTP 응답
  - `@ResponseBody` 혹은 `HttpEntity` 담당 `ReturnValueHandler`
  - 위의 친구들이 메시지 컨버터를 호출해 HTTP 응답 결과를 생성
- `@RequestBody` `@ResponseBody` 의 컨버팅은 `RequestResponseBodyMethodProcessor`
- `HttpEntity`의 컨버팅은 `HttpEntityMethodProcessor` 이다.

### 스프링 MVC 확장

- 기능 확장은 `WebMvcConfigurer` 를 상속 받아서 스프링 빈으로 등록하면 된다.
- 인터페이스
  - HandlerMethodArgumentResolver
  - HandlerMethodReturnValueHandler
  - HttpMessageConverter

## 스프링 MVC - 백엔드 웹 개발 활용 기술

### 타임 리프

- 서버 사이드 렌더링(SSR): 서버에서 HTML을 동적으로 렌더링 하는 용도
- 네추럴 템플릿(natural templates): 순수 HTML을 유지하는 템플릿. 서버 랜더링 하지 않고 html 파일을 열어도 브라우져에서 볼 수 있음
- 스프링 통합 지원: 다음 섹션에서 적어 보자!(TODO)

### 텍스트 - text, utext

기본적인 텍스트 치환하여 출력

#### 주의 - Escape

- 우리가 바라는 것(강조) : `Hello <b>Spring!</b>`
- 실제 출력되는 것(바뀜!) : `Hello &lt;b&gt;Spring!&lt;/b&gt;`

- HTML 엔티티 
  - `<` 를 테그의 시작으로 인식
  - 우리는 그대로 보여주길 원함

#### Un-escape

- 이스케이프 기능을 끔!
- 타임리프의 기능 지원
  - `th:text` -> `th:utext`
  - `[[...]]` -> `[(...)]`

### 변수 - SpringEL

- Object
  - `${user.username}` -> user의 username을 프로퍼티 접근
  - `${user['username']}` = 위와 같음
  - `${user.getUsername()}` = user의 `getUsername()` 을 직접 호출
- List
  - `${users[0].username}` = List에서 첫 번째 회원을 찾고 username 프로퍼티 접근
  - `${users[0]['username']}` = 위와 같음
  - `${users[0].getUsername()}` = List에서 첫 번째 회원을 찾고 메서드 직접 호출
- Map
  - `${userMap['userA'].username}` = Map에서 userA를 찾고, username 프로퍼티 접근
  - `${userMap['userA']['username']}` = 위와 같음
  - `${userMap['userA'].getUsername()}` = Map에서 userA를 찾고 메서드 직접 호출

#### 지역 변수 선언

- `th:with`로 선언해 사용.
- 선언한 테그 안에서만 사용 가능

### 기본 객체들

- `${#request}`
- `${#response}`
- `${#session}`
- `${#servletContext}`
- `${#locale}`

- 편의 객체 : requset 객체에서 꺼내는게 귀찮으므로 쉽게 조회하도록 함
- HTTP 요청 파라미터 접근: `param`
  - 예) `${param.paramData}`
- HTTP 세션 접근: `session`
  - 예) `${session.sessionData}`
- 스프링 빈 접근: `@`
  - 예) `${@helloBean.hello('Spring!')}`

### 유틸리티 객체와 날짜

| 필요할 때 찾아서 사용하면 된다.
| 공식 문서에서 예제를 찾아서 쓰자!

- #message : 메시지, 국제화 처리
- #uris : URI 이스케이프 지원
- #dates : java.util.Date 서식 지원 #calendars : java.util.Calendar 서식 지원 #temporals : 자바8 날짜 서식 지원
- #numbers : 숫자 서식 지원
- #strings : 문자 관련 편의 기능
- #objects : 객체 관련 기능 제공
- #bools : boolean 관련 기능 제공
- #arrays : 배열 관련 기능 제공
- #lists , #sets , #maps : 컬렉션 관련 기능 제공 #ids : 아이디 처리 관련 기능 제공, 뒤에서 설명

#### 자바8 날짜

- `thymeleaf-extras-java8time` : 자바8 날짜 지원 라이브러리
- `#temporals` : 자바8 날짜용 유틸리티 객체

### URL 링크

- 단순한 URL
  - `@{/hello}` -> `/hello`
- 쿼리 파라미터
  - `@{/hello(param1=${param1}, param2=${param2})}` -> `/hello?param1=data1&param2=data2`
  - () 에 있는 부분은 쿼리 파라미터로 처리된다.
- 경로 변수
  - `@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}` -> `/hello/data1/data2`
  - URL 경로상에 변수가 있으면 () 부분은 경로 변수로 처리된다.
- 경로 변수 + 쿼리 파라미터
  - `@{/hello/{param1}(param1=${param1}, param2=${param2})}` -> `/hello/data1?param2=data2`
  - 경로 변수와 쿼리 파라미터를 함께 사용할 수 있다.
- 상대경로, 절대경로, 프로토콜 기준을 표현할 수 도 있다.
  - `/hello` : 절대 경로
  - `hello` : 상대 경로

### 리터럴

- 리터럴: 소스 코드상에 고정된 값
- 쉬워 보이지만 많이 실수하니 잘 정리하자.

```java
String a = "Hello"
int a = 10 * 20
```

- 문자: `'hello'`
- 숫자: `10`
- 불린: `true` , `false`
- null: `null`

- *(주의)* 타임리프에서 문자 리터럴은 항상 ' (작은 따옴표)로 감싸야 한다.
  - `<span th:text="'hello'">`
- 이건 귀찮은 경우가 많으므로 *공백없이 쭉 이어진다면* 하나의 의미있는 토큰으로 인지해서 다음과 같이 작은 따옴표를 생략할 수 있다.
  - 룰: A-Z, a-z, 0-9, [], ., -, _
  - `<span th:text="hello">`

- 오류 : `<span th:text="hello world!"></span>` -> 수정 : `<span th:text="'hello world!'"></span>`

- 리터럴 대체(Literal substitutions): `<span th:text="|hello ${data}|">`

### 연산

- 비교연산: HTML 엔티티를 사용해야 하는 부분을 주의하자,
  - > (gt), < (lt), >= (ge), <= (le), ! (not), == (eq), != (neq, ne)
- 조건식: 자바의 조건식과 유사하다.
- Elvis 연산자: 조건식의 편의 버전
- No-Operation: _ 인 경우 마치 타임리프가 실행되지 않는 것 처럼 동작한다. 이것을 잘 사용하면 HTML 의 내용 그대로 활용할 수 있다. 마지막 예를 보면 데이터가 없습니다. 부분이 그대로 출력된다.

### 속성 값 설정

여기서 말하는 속성은 html 테그의 속성(attribute)을 말함

타임리프는 `th:*` 로 속성을 지정하면 기존 속성을 대체하거나 없으면 새로 만든다.

`<input type="text" name="mock" th:name="userA" />` -> 타임리프 렌더링 -> `<input type="text" name="userA" />`

#### 속성 추가

- `th:attrappend` : 속성 값의 앞에 값을 추가
- `th:attrprepend` : 속성 값의 뒤에 값을 추가
- `th:classappend` : class 속성에 자연스럽게 추가

#### checked 처리

- HTML 에서는 `<input type="checkbox" name="active" checked="false" />` -> HTML 에서 checked 속성은 checked 속성의 값에 상관없이 실제 체크박스에 체크되어 있다.
- true, false 여부로 판단하는 개발자 입장에선 불편하다.
- 타임리프의 `th:checked` 는 값이 `false` 인 경우 checked 속성을 제거한다.

`<input type="checkbox" name="active" th:checked="false" />` -> 타임리프 렌더링 -> `<input type="checkbox" name="active" />`

### 반복 기능

`<tr th:each="user : ${users}">`
- 반복시 컬랙션 ${users} 의 값을 하나씩 꺼내서 왼쪽 변수(user) 에 담아 테그를 반복실행한다.
- List 뿐만 아니라 Iterable, Enumeration 을 구현한 모든 객체를 반복에 사용 가능하다. Map 도 사용가능(변수에 담기는 값은 Map.Entry)

#### 반복 상태 유지

`<tr th:each="user, userStat : ${users}">`

- 반복의 두번째 파라메터는 반복의 상태 확인에 쓴다.
- 생략 가능하다 생력하면 `변수명(user) + Stat` 이 된다.
- index : 0 부터 시작하는 값
- count : 1 부터 시작하는 값
- size : 전체 사이즈
- even, odd : 홀수, 짝수 여부(boolean)
- first, last : 처음, 마지막 여부(boolean)
- current: 현재 객체

### 조건부 평가

- if, unless
  - 타임리프는 해당 조건이 맞지 않으면 태그 자체를 렌더링하지 않는다.
  - `<span th:text="' 성년 '" th:if="${user.age lt 20}"></span>`
- switch
  - `*` 은 만족하는 조건이 없을 때 사용하는 디폴트이다.
