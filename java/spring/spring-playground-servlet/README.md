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

### HTTP 응답 데이터

1. 단순 텍스트
2. HTML 응답
3. HTTP API - MessageBody에 JSON 응답

## MVC 프레임워크 만들기

- v1: 프론트 컨트롤러 도입
    - 기존 구조를 최대한 유지하면서 프론트 컨트롤러 도입
      ![v1](img/v1.png)
- v2: View 분류
    - 단순 반복 뷰 로직 분리
      ![v2](img/v2.png)
- v3: Model 추가
    - 서블릿 종속성 제거
    - 뷰 이름 중복 제거 
    - 프레임워크나 공통 기능이 수고로워야 사용하는 개발자가 편리해진다.
      ![v3](img/v3.png)
- v4: 단순하고 실용적인 컨트롤러
    - v3와 비슷
    - 구현 입장에서 ModelView를 직접 생성해서 반환하지 않도록 편리한 인터페이스 제공
      ![v4](img/v4.png)
- v5: 유연한 컨트롤러
    - 어댑터 도입
    - 어댑터를 추가해서 프레임워크를 유연하고 확장성 있게 설계
      ![v5](img/v5.png)

여기에 에노테이션 스타일로 컨트롤러를 만들려면 에노테이션 어뎁터를 추가하면 된다.
