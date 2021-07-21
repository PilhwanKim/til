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
