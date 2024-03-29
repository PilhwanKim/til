# Spring : 스프링 타입 컨버터

## 스프링 타입 컨버터 소개

### 스프링의 타입 변환 적용 예

- 스프링 MVC 요청 파라미터
  - @RequestParam
  - @ModelAttribute
  - @PathVariable
- @Value 등으로 YML 정보 읽기
- XML에 넣은 스프링 빈 정보를 변환
- 뷰를 렌더링 할 때

### @RequestParam 예

```java
@GetMapping("/hello-v2")
public String helloV2(@RequestParam Integer data) {
```

### @ModelAttribute 타입 변환 예

```java
@ModelAttribute UserData data

class UserData {
    Integer data;
}
```

### @PathVariable 타입 변환 예시

```java
/users/{userId}
  
@PathVariable("data") Integer data
```

### 스프링과 타입 변환

만약 개발자가 새로운 타입을 만들어서 변환하고 싶으면?

이를 위해 스프링은 확장 가능한 컨버터 인터페이스를 제공

```java
package org.springframework.core.convert.converter;
public interface Converter<S, T> {
    T convert(S source);
}

```

## 타입 컨버터 - Converter

StringToIntegerConverter - 문자를 숫자로 변환하는 타입 컨버터
(코드 참고)

IntegerToStringConverter - 숫자를 문자로 변환하는 타입 컨버터
(코드 참고)

StringToIpPortConverter - Ip 문자열을 IpPort 객체로 변환
(코드 참고)

IpPortToStringConverter -  IpPort 객체를 Ip 문자열로 변환
(코드 참고)

이쯤되면...
타입 컨버터를 등록하고 관리하면서 편리하게 변환 기능을 제공하는 역할을 하는 무언가가 필요하다.

> 참고
>
> 스프링은 문자, 숫자, 불린, Enum등 일반적인 타입에 대한 대부분의 컨버터를 기본으로 제공한다. IDE에서 `Converter` , `ConverterFactory` , `GenericConverter` 의 구현체를 찾아보면 수 많은 컨버터를 확인할 수 있다.

## 컨버전 서비스 - ConversionService

- 직접 타입 컨버터를 사용하는 것은 불편함.
- 개별 컨버터를 모아두고 편리하게 사용할 수 있는 기능을 제공

### 등록과 사용 분리

컨버터를 등록할 때는 `StringToIntegerConverter` 같은 타입 컨버터를 명확하게 알아야 한다. 반면에 컨버터를 사용하는 입장에서는 타입 컨버터를 전혀 몰라도 된다. 타입 컨버터들은 모두 컨버전 서비스 내부에 숨어서 제공된다. 따라서 타입을 변환을 원하는 사용자는 컨버전 서비스 인터페이스에만 의존하면 된다. 물론 컨버전 서비스를 등록하는 부분과 사용하는 부분을 분리하고 의존관계 주입을 사용해야 한다.

### 컨버전 서비스 사용

```java
Integer value = conversionService.convert("10", Integer.class)
```

### 인터페이스 분리 원칙 - ISP(Interface Segregation Principal)

클라이언트가 자신이 이용하지 않는 메서드에 의존하지 않아야 한다.

`DefaultConversionService` 는 다음 두 인터페이스를 구현했다
- `ConversionService` : 컨버터 사용에 초점
- `ConverterRegistry` : 컨버터 등록에 초점

이렇게 인터페이스를 분리하면 컨버터를 사용하는 클라이언트와 컨버터를 등록하고 관리하는 클라이언트의 관심사를 명확하게 분리할 수 있다. 특히 컨버터를 사용하는 클라이언트는 ConversionService 만 의존하면 되므로, 컨버터를 어떻게 등록하고 관리하는지는 전혀 몰라도 된다. 결과적으로 컨버터를 사용하는 클라이언트는 꼭 필요한 메서드만 알게된다. 

스프링은 내부에서 `ConversionService` 를 사용해서 타입을 변환한다. 예를 들어서 앞서 살펴본 `@RequestParam` 같은 곳에서 이 기능을 사용해서 타입을 변환한다.

## 스프링에 Converter 적용하기

WebConfig - 컨버터 등록(소스 참고)

HelloController 에 IpPort 객체 `@RequestParam` 적용(소스 참고)

### Converter 처리 과정

`@RequestParam` 은 @`RequestParam` 을 처리하는 `ArgumentResolver` 인 `RequestParamMethodArgumentResolver` 에서 `ConversionService` 를 사용해서 타입을 변환한다.

## 뷰 템플릿에 컨버터 적용하기

이전까지는 문자를 객체로 변환했다면, 이번에는 그 반대로 객체를 문자로 변환하는 작업을 확인할 수 있다.

### 뷰 렌더링(타임리프)

- 변수 표현식 : `${...}`
- 컨버전 서비스 적용 : `${{...}}`

#### 실행 결과

- `${{number}}` : 뷰 템플릿은 데이터를 문자로 출력한다. 따라서 컨버터를 적용하게 되면 Integer 타입인 10000 을 String 타입으로 변환하는 컨버터인 `IntegerToStringConverter` 를 실행하게 된다. 이 부분은 컨버터를 실행하지 않아도 타임리프가 숫자를 문자로 자동으로 변환히기 때문에 컨버터를 적용할 때와 하지 않을 때가 같다.
- `${{ipPort}}` : 뷰 템플릿은 데이터를 문자로 출력한다. 따라서 컨버터를 적용하게 되면 IpPort 타입을 String 타입으로 변환해야 하므로 `IpPortToStringConverter` 가 적용된다. 그 결과 `127.0.0.1:8080` 가 출력된다

### 폼에 적용하기

타임리프의 `th:field` 는 앞서 설명했듯이 id , name 를 출력하는 등 다양한 기능이 있는데, 여기에 **컨버전 서비스**도 함께 적용된다.

#### 폼 실행 결과

- `GET /converter/edit`
  - `th:field` 가 자동으로 컨버전 서비스를 적용해주어서 `${{ipPort}}` 처럼 적용이 되었다. 따라서 IpPort String 으로 변환된다.
- `POST /converter/edit`
  - `@ModelAttribute` 를 사용해서 `String` -> `IpPort` 로 변환된다

## 포맷터 - Formatter

- `Converter`: 입력과 출력 타입에 제한이 없는, 범용 타입 변환 기능
- `Formatter`: 객체를 특정한 포멧에 맞추어 문자로 출력하거나 또는 그 반대의 역할을 하는 것에 특화된 기능. 
  - 문자에 특화한 Converter

### 포맷터 - Formatter 만들기

#### Formatter 인터페이스

```java
public interface Printer<T> {
    String print(T object, Locale locale);
}
  public interface Parser<T> {
    T parse(String text, Locale locale) throws ParseException;
}
  public interface Formatter<T> extends Printer<T>, Parser<T> {
}
```

- `String print(T object, Locale locale)` : 객체를 문자로 변경한다.
- `T parse(String text, Locale locale)` : 문자를 객체로 변경한다.

#### MyNumberFormatter 구현

(소스 참고)

## 포맷터를 지원하는 컨버전 서비스

- 일반적인 컨버전 서비스에는 포맷터를 등록할 수는 없다.
- `FormattingConversionService` 는 포맷터를 지원하는 컨버전 서비스
- `DefaultFormattingConversionService` 는 `FormattingConversionService` 에 기본적인 통화, 숫자 관련 몇가지 기본 포맷터를 추가해서 제공

### DefaultFormattingConversionService 상속 관계

- `FormattingConversionService` 는 `ConversionService` 관련 기능을 상속받기 때문에 결과적으로 **컨버터도 포맷터도 모두 등록**할 수 있다.
- 사용할 때는 `ConversionService` 가 제공하는 `convert` 를 사용하면 된다.
- 스프링 부트는 `DefaultFormattingConversionService` 를 상속 받은 `WebConversionService` 를 내부에서 사용한다.

## 포맷터 적용하기

### WebConfig 에서 추가

(소스 참고)
### 실행: 객체 -> 문자

http://localhost:8080/converter-view

### 실행: 문자 -> 객체

http://localhost:8080/hello-v2?data=10,000

## 스프링이 제공하는 기본 포맷터

- `Formatter` 인터페이스의 구현 클래스를 찾아보면 수 많은 날짜나 시간 관련 포맷터가 제공
- 포맷터는 기본 형식이 지정되어 있기 때문에, 객체의 각 필드마다 다른 형식으로 포맷을 지정하기는 어렵다
  - 그래서 2가지 유용한 애노테이션 기반 포멧터가 있다.
    - `@NumberFormat` : 숫자 관련 형식 지정 포맷터 사용, `NumberFormatAnnotationFormatterFactory` 
    - `@DateTimeFormat` : 날짜 관련 형식 지정 포맷터 사용, `Jsr310DateTimeFormatAnnotationFormatterFactory`

> 참고
>
> `@NumberFormat` , `@DateTimeFormat` 의 자세한 사용법이 궁금한 분들은 다음 링크를 참고하거나 관련 애노테이션을 검색해보자.
> https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#format-CustomFormatAnnotations

## 마지막 주의점

메시지 컨버터(`HttpMessageConverter`)에는 컨버전 서비스가 적용되지 않는다.
특히 객체를 JSON으로 변환할 때 메시지 컨버터를 사용하면서 이 부분을 많이 오해하는데,  `HttpMessageConverter` 의 역할은 HTTP 메시지 바디의 내용을 객체로 변환하거나 객체를 HTTP 메시지 바디에 입력하는 것이다. 예를 들어서 JSON을 객체로 변환하는 메시지 컨버터는 내부에서 Jackson 같은 라이브러리를 사용한다. 객체를 JSON으로 변환한다면 그 결과는 이 라이브러리에 달린 것이다. 따라서 JSON 결과로 만들어지는 숫자나 날짜 포맷을 변경하고 싶으면 해당 라이브러리가 제공하는 설정을 통해서 포맷을 지정해야 한다. 

결과적으로 이것은 컨버전 서비스와 전혀 관계가 없다.

컨버전 서비스는 `@RequestParam`, `@ModelAttribute`, `@PathVariable`, 뷰 템플릿 등에서 사용할 수 있다.