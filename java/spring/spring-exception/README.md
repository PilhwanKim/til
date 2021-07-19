# Spring MVC : 예외 처리

## 서블릿 예외 처리 - 시작

순수 서블릿 컨테이너는 다음 2가지 방식으로 예외를 처리함

- `Exception` (예외)
- `response.sendError(HTTP 상태 코드, 오류 메시지)`

### Exception

자바 직접 실행

- 메인 메서드를 실행 -> `main` 스레드가 실행
- 실행 도중 예외가 `main()` 메서드를 넘어서 예외가 던져지면, 예외정보를 남기고 스레드가 종료된다.

웹 어플리케이션

- 사용자 요청별로 별도의 스레드가 할당됨, 서블릿 컨테이너 안에서 실행
- 에플리케이션이 예외를 잡지 못하고, 서블릿 밖으로 예외가 전달되면?
- `WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)`
- **이 경우 서버 내부에서 처리할 수 없는 오류가 발생한 것으로 생각해서 `HTTP 상태 코드 500`을 반환한다.**

### response.sendError(HTTP 상태 코드, 오류 메시지)

- 당장 예외가 발생하는 것은 아니지만, 서블릿 컨테이너에게 오류가 발생했다는 점을 전달할 수 있다.
- 이 메서드를 사용하면 HTTP 상태 코드와 오류 메시지도 추가할 수 있다.

#### sendError 흐름

```
WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러
(response.sendError())
```

- `response.sendError()` 를 호출하면 response 내부에는 오류가 발생했다는 상태를 저장
- 서블릿 컨테이너는 고객에게 응답 전에 response 에 sendError() 가 호출되었는지 확인
- 호출되었다면 설정한 오류 코드에 맞추어 기본 오류 페이지를 보여준다

## 서블릿 예외 처리 - 오류 화면 제공

서블릿이 제공하는 오류 화면 기능을 사용

과거에는 web.xml 이라는 파일에 다음과 같이 오류 화면을 등록했다.

```xml
<web-app>
      <error-page>
        <error-code>404</error-code>
        <location>/error-page/404.html</location>
      </error-page>
      <error-page>
        <error-code>500</error-code>
        <location>/error-page/500.html</location>
      </error-page>
      <error-page>
        <exception-type>java.lang.RuntimeException</exception-type>
        <location>/error-page/500.html</location>
      </error-page>
</web-app>
```

지금은 스프링 부트 기능을 이용하여 등록할 수 있다.

```java

@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/400");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
```

- `response.sendError(404)` : errorPage404 호출 
- `response.sendError(500)` : errorPage500 호출 
- `RuntimeException 또는 그 자식 타입의 예외`: errorPageEx 호출
  - **오류 페이지는 예외를 다룰 때 해당 예외와 그 자식 타입의 오류를 함께 처리한다**

