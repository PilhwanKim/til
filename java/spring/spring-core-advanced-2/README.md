# 스프링 핵심 원리 - 고급편 2

## 예제 만들기

- 예제는 크게 3가지 상황으로 만든다.
  - v1 - 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록 
  - v2 - 인터페이스 없는 구체 클래스 - 스프링 빈으로 수동 등록 
  - v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록

### v1 - 인터페이스와 구현 클래스 예제

- `OrderControllerV1`
  - @RequestMapping, @ResponseBody 는 인터페이스에 사용가능
  - @Controller 컴포넌트 스캔 대상이기 때문
  - 인터페이스에는 `@RequestParam("itemId")` 명시적으로 적용해야 함
  - `request()` 는 LogTrace 를 적용할 대상
  - `noLog()` 는 LogTrace 를 적용하지 않을 대상
- 빈 수동 등록
  - `AppV1Config`
  - `@Import(AppV1Config.class)`
    - AppV1Config 클래스를 스프링 빈으로 등록
    - 설정 파일을 등록하므로, 설정파일 하위 빈들도 같이 등록
  - `@SpringBootApplication(scanBasePackages = "dev.leonkim.proxy.app")`
    - 컴포넌트 스켄의 패키지 범위 지정: `dev.leonkim.proxy.app`
    - 왜? `dev.leonkim.proxy.trace` 나 `dev.leonkim.proxy.config` 패키지는 나중에 선택적으로 빈 등록 위함
    
### v2 - 인터페이스 없는 구체 클래스

- `AppV2Config`, `OrderControllerV2`, `OrderServiceV2`, `OrderRepositoryV2` 예제코드