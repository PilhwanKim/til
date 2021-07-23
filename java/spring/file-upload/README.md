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

## 서블릿과 파일 업로드

### ServletUploadControllerV1

(소스코드 참고)

### 멀티파트 사용 옵션

업로드 사이즈 제한

```properties
spring.servlet.multipart.max-file-size=1MB
spring.servlet.multipart.max-request-size=10MB
```

- `max-file-size` : 파일 하나의 최대 사이즈, 기본 1MB
- `max-request-size` : 멀티파트 요청 하나에 여러 파일을 업로드 할 수 있는데, 그 전체 합이다. 기본 10MB
- 사이즈를 넘으면 예외( `SizeLimitExceededException` )가 발생

`spring.servlet.multipart.enabled=true` (기본 true)

> 참고
>
> `spring.servlet.multipart.enabled` 옵션을 켜면 스프링의 `DispatcherServlet` 에서 멀티파트 리졸버( `MultipartResolver` )를 실행한다.
> 멀티파트 리졸버는 멀티파트 요청인 경우 서블릿 컨테이너가 전달하는 일반적인 `HttpServletRequest` 를 `MultipartHttpServletRequest` 로 변환해서 반환한다.
> `MultipartHttpServletRequest` 는 `HttpServletRequest` 의 자식 인터페이스이고, 멀티파트와 관련된 추가 기능을 제공한다.
> 스프링이 제공하는 기본 멀티파트 리졸버는 `MultipartHttpServletRequest` 인터페이스를 구현한 `StandardMultipartHttpServletRequest` 를 반환한다.
> 이제 컨트롤러에서 `HttpServletRequest` 대신에 `MultipartHttpServletRequest` 를 주입받을 수 있는데, 이것을 사용하면 멀티파트와 관련된 여러가지 처리를 편리하게 할 수 있다. 그런데 이후 강의에서 설명할 MultipartFile 이라는 것을 사용하는 것이 더 편하기 때문에 `MultipartHttpServletRequest` 를 잘 사용하지는 않는다. 더 자세한 내용은 `MultipartResolver` 를 검색해보자.
