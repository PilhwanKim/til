# 실전! 스프링 데이터 JPA

김영한 님의 인프런 강좌 `실전! 스프링 데이터 JPA` 를 실습한 프로젝트 입니다.
(https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84)

## 공통 인터페이스

![공통 인터페이스 구조](./img/common-interface.png)

- 패키지 구조를 유심히 보자!
- 공통 인터페이스는 기본 CRUD의 반복 작업을 줄이는(노가다를 줄이는) 기술
- `save(S)`: 새로운엔티티 저장, 이미 있는 엔티티는 merge
- `getOne(ID)` : 엔티티 프록시 조회. `EntityManager.getReference()` 호출
- `findAll(...)` : 모든 엔티티 조회. 정렬(`Sort`), 페이징(`Pageable`) 조건 파라메터로 제공

## 메소드 이름으로 쿼리 생성

메소드 이름으로 where, count, exists, limit 절 등을 정의

Query Creation: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

### 필요성
- 조건이 2개 이하일때만 유효하다. 2개 이상이면 QueryDSL 로 푼다.
- 짧은 쿼리를 빠르게 만들때 필요


## NamedQuery

- NamedQuery -> 메소드 이름 쿼리 생성 우선순위 순으로 쿼리 생성
- 장점: application 로딩 시점에 JPQL의 오류를 찾아낸다.
- 실무에선 크게 사용성이 떨어짐 (@Query 로 대체 가능하기 때문)

## @Query

- 장점
  - application 로딩 시점에 JPQL의 오류를 찾아낸다(NamedQuery랑 같음)
  - Repository 에 직접 쿼리를 작성함으로 보기 편함.
  - 메소드 이름으로 작성하는 것에 비해 조건이 길어지면 유리하다
