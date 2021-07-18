# 스프링 MVC - 로그인 처리

## 로그인 요구사항

- 홈 화면 - 로그인 전 회원 가입
  - 로그인
  - 홈 화면
- 로그인 후
  - 본인 이름(누구님 환영합니다.)
  - 상품 관리
  - 로그 아웃
- 보안 요구사항
  - 로그인 사용자만 상품에 접근하고, 관리할 수 있음
  - 로그인 하지 않은 사용자가 상품 관리에 접근하면 로그인 화면으로 이동
- 회원 가입, 상품 관리

## 로그인 처리하기 - 쿠키 사용

쿠키 생성
![쿠키 생성](./image/cookie-1.png)

클라이언트 쿠키 전달 1
![클라이언트 쿠키 전달 1](./image/cookie-2.png)

클라이언트 쿠키 전달 2
![클라이언트 쿠키 전달 2](./image/cookie-3.png)

쿠키 종류
- 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
- 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지

쿠키 생성 로직

```java
Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
response.addCookie(idCookie);
```

로그인에 성공하면 쿠키를 생성하고 `HttpServletResponse` 에 담는다. 쿠키 이름은 `memberId` 이고, 값은 `회원의 id`를 담아둔다.웹브라우저는 종료전까지 회원의 id를 서버에 계속 보내줄것이다.

로그아웃 기능 - 쿠키 지우기

```java
Cookie cookie = new Cookie(cookieName, null);
cookie.setMaxAge(0);
response.addCookie(cookie);
```

로그아웃도 응답 쿠키를 생성하는데 Max-Age=0 를 확인할 수 있다. 해당 쿠키는 즉시 종료된다.
