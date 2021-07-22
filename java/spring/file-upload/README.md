# Spring : 파일 업로드

## 파일 업로드 소개

HTML 폼 전송 방식

- `application/x-www-form-urlencoded`
- `multipart/form-data`

### application/x-www-form-urlencoded 방식

![application/x-www-form-urlencoded 방식](./image/upload-method-1.png)

- 파일을 업로드 하려면 파일은 문자가 아니라 **바이너리 데이터**를 전송
- 보통 폼을 전송할 때 파일만 전송하는 것이 아님
  - 예)
    - 이름
    - 나이
    - 첨부파일
- **문자와 바이너리를 동시에 전송하는 수단**이 필요함

### multipart/form-data 방식

![application/x-www-form-urlencoded 방식](./image/upload-method-2.png)

- Form 태그에 별도의 `enctype="multipart/form-data"` 를 지정
- 다른 종류의 여러 파일과 폼의 내용 함께 전송
- `Content-Disposition` 항목별 헤더, 부가정보
  - 이름, 나이 : 항목별로 문자가 전송
  - 첨부 파일 : 이름, Content-Type, 바이너리 데이터
- 각각의 항목(**Part**)을 구분해서, 한번에 전송
