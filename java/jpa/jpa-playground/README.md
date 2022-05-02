# 자바 ORM 표준 JPA 프로그래밍 - 기본편

김영한 님의 인프런 강좌 `자바 ORM 표준 JPA 프로그래밍 - 기본편` 을 실습한 프로젝트 입니다.
(https://www.inflearn.com/course/ORM-JPA-Basic)

## Setup

- MacOS 환경에서 Mysql 설치 및 구동
```shell
# mysql 설치
$ brew install mysql@5.7
# mysql 시작
$ brew services start mysql@5.7
```

- Mysql 에서 schema 생성
```sql
create schema jpa_playground collate utf8_general_ci;
```
