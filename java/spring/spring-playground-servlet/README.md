# 스프링 MVC : 백엔드 웹 개발 핵심 기술

아래 강의 실습 프로젝트이다.
https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1

## 서블릿

### HTTP 요청 데이터

1. GET - 쿼리 파라메터
    - /url?username=hello&age=20
    - 메시지 바디 X, URL 쿼리 파라메터에 데이터 포함
    - 예) 검색, 필터, 페이징 등

2. POST HTML Form 전송 방식
    - content-type:application/x-www-form-urlencoded
    - 메시지 바디에 쿼리 파라메터 형식으로 요청

3. HTTP message body 에 데이터 직접 담음
    - HTTP API 사용. JSON, XML, TEXT
    - 주로 JSON
    - POST, PUT, PATCH

### HTTP 응답

- HTTP 응답 코드 지정
- 헤더 생성
- 바디 생성
- 편의 기능 제공
   - Content-type
   - Cookie
   - Redirect
   
