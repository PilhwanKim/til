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
