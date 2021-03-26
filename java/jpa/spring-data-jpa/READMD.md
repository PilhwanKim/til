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

## 파라메터 바인딩

- 이름 기반 vs 위치 기반? -> 무조건 이름 기반으로..
- 컬렉션 파라메터 바인딩

## 반환 타입

- 컬랙션 반환에서 결과가 없으면 `size()` 는 0인 빈 컬랙션 반환된다.  
- JPA 에서는 `getSingleResuit()` 결과가 없다면 `NoResultException`이 발생
- Spring Data Jpa 에서 1개 반환 타입이면 `null` 이 온다.
- 자세한 것 : https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repository-query-return-types

## 페이징과 정렬

### 파라미터

- `org.springframework.data.domain.Sort` : 정렬기능
- `org.springframework.data.domain.Pageable` : 페이징 기능(내부 `Sort` 포함)
  - page index 는 `0` 부터 시작한다.

### 특별한 반환타입

- `org.springframework.data.domain.Page` : count 쿼리 결과 포함된 페이징
- `org.springframework.data.domain.Slice` : count 쿼리 없음. 다음 페이지 확인 가능
- `List` : 추가 count 쿼리 없이 결과만 반환
- *(중요)* API 단에 반환하지 마라. DTO 로 변환하여 반환해라.

## 벌크성 수정 쿼리

- `@Modifying` 
  - `@Query` 가 변경 쿼리임을 선언해 줘야 함
  - `(주의)` 벌크 연산 전의 영속성 컨텍스트와 불일치 이슈 존재 -> clear()를 꼭 해야주어야 한다.

## @EntityGraph

- 지연 로딩 - 연관관계를 실제 사용할 때 DB 조회를 함
  - 지연 상태의 엔티티는 (임시로) 엔티티 프록시 객체로 채워 놓는다(초기화 한다).
  - 실제 연관관계 객체의 프로퍼티를 사용할 때! -> 조회쿼리를 실행(lazy loading)한다. 
- 지연 로딩 인한 문제점
  - N + 1 문제 : 지연로딩으로 인해 나중에 추가로 N건 으로 조회를 나중에 하는 것
- 해결책 : 페치 조인
  - JPQL 의 join fetch
  - 패치조인 기능 wrapper `@EntityGraph`
- 응용
  - 간단한 join 쿼리 경우 `@EntityGraph`
  - 복잡해지면 JPQL(`@Query`) 에서 직접 지정이 더 낫다. 

## JPA Hint & Lock

### JPA Hint

- 예) `@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))`
  - dirty checking skip
- QueryHint 는 JPA 구현체의 특정 기능 사용 가능하게 구멍을 만듦

### Lock

- `select for update` - 비관적 락: 해당 레코드를 변경 시도할 때 잠시 막음
- 자세한 것은 영한님 JPA 책 마지막 장 보기
- 응용: 실시간 서비스에는 지양, 정합성이 중요한 경우에 쓴다.

## 확장 기능

### 사용자 정의 리포지토리 구현

- 스프링 데이터 JPA 리포지토리는 인터페이스로만 정의됨, 구현은 없음
- 스프링 데이터 JPA 리포지토리로 한계가 있는 기능이 존재
  - 복잡한 쿼리(통계)
  - 동적 쿼리
  - etc..
- 다양한 이유로 인터페이스의 메서드를 직접 구현하고 싶을 때
  - JPA 직접 사용
  - JDBCTemplate
  - MyBatis
  - 데이터베이스 커넥션 직접 사용
  - (추천) Querydsl 사용
  

- 규칙!: 구현체의 class 명
  - (과거) `{리포지토리 인터페이스 이름} + Impl`
  - (추천) `{사용자 정의 인터페이스 명} + Impl`

  
- Impl 대신 다른 이름으로 변경하고 싶으면?(왠만하면 관례를 따라 만드는걸 권장!)
```java
@EnableJpaRepositories(basePackages = "study.datajpa.repository",  repositoryImplementationPostfix = "Impl")
```

#### 응용
- 모든 레포지토리를 사용자 정의로 만들진 말자.
- 성격에 따라 아예 분리하는게 나을 수 있다.
  - 커멘드 vs 쿼리 분리
  - 핵심 비즈니스로직 vs 그 외(단순 화면조회용) 
