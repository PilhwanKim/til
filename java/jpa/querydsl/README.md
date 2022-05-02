# 실전! Querydsl

김영한 님의 인프런 강좌 `실전! Querydsl` 를 실습한 프로젝트 입니다.
(https://www.inflearn.com/course/Querydsl-%EC%8B%A4%EC%A0%84)

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
create schema querydsl collate utf8_general_ci;
```
