# 스프링 MVC -웹 페이지 만들기

## 요구사항 분석

### 상품 도메인 모델
- 상품 ID 
- 상품명 
- 가격 
- 수량

### 상품 관리 기능
- 상품 목록
- 상품 상세
- 상품 등록
- 상품 수정

![서비스 제공 흐름](./img/service-flow.png)

## 타임리프
- 네츄럴 템플릿 = 순수 HTML 그대로 유지 + 뷰 템플릿도 사용

### 타임리프 핵심
- 핵심은 th:xxx 가 붙은 부분은 서버사이드에서 렌더링 되고, 기존 것을 대체한다. th:xxx 이 없으면 기존 html의 xxx 속성이 그대로 사용된다.
- HTML을 파일로 직접 열었을 때, th:xxx 가 있어도 웹 브라우저는 ht: 속성을 알지 못하므로 무시한다. 따라서 HTML을 파일 보기를 유지하면서 템플릿 기능도 할 수 있다.

### URL 링크 표현식
- URL 링크 표현식 - `@{...}, th:href="@{/css/bootstrap.min.css}"`
- `@{...}` : 타임리프는 URL 링크를 사용하는 경우 @{...} 를 사용한다. 이것을 URL 링크 표현식이라 한다. URL 링크 표현식을 사용하면 서블릿 컨텍스트를 자동으로 포함한다.

### URL 링크 표현식2 - @{...}, 
- `th:href="@{/basic/items/{itemId}(itemId=${item.id})}"`
- 상품 ID를 선택하는 링크를 확인해보자.
- URL 링크 표현식을 사용하면 경로를 템플릿처럼 편리하게 사용할 수 있다.
- 경로 변수( {itemId} ) 뿐만 아니라 쿼리 파라미터도 생성한다.
- 예) `th:href="@{/basic/items/{itemId}(itemId=${item.id}, query='test')}"`
    - 생성 링크: `http://localhost:8080/basic/items/1?query=test`

### 리터럴 대체 - |...|
- `|...|` :이렇게 사용한다.
- 타임리프에서 문자와 표현식 등은 분리되어 있기 때문에 더해서 사용해야 한다.
    - `<span th:text="'Welcome to our application, ' + ${user.name} + '!'">`
- 다음과 같이 리터럴 대체 문법을 사용하면, 더하기 없이 편리하게 사용할 수 있다.
    - `<span th:text="|Welcome to our application, ${user.name}!|">`
  
## POST - HTML Form
- `content-type: application/x-www-form-urlencoded`
- 메시지 바디에 쿼리 파리미터 형식으로 전달 `itemName=itemA&price=10000&quantity=10`
- 예) 회원 가입, 상품 주문, HTML Form 사용

### @ModelAttribute - 요청 파라미터 처리

- `@ModelAttribute` 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.

### @ModelAttribute - Model 추가

- 모델(Model)에 `@ModelAttribute` 로 지정한 객체를 자동으로 넣어준다. 
- `model.addAttribute("item", item)` 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.
- `@ModelAttribute` 의 이름을 생략하면 모델에 저장될 때 클래스명을 사용한다. 이때 클래스의 첫글자만 소문자로 변경해서 등록한다. 

## 리다이렉트

- 상품 수정은 마지막에 뷰 템플릿을 호출하는 대신에 상품 상세 화면으로 이동하도록 리다이렉트를 호출한다.
- 스프링은 `redirect:/...` 으로 편리하게 리다이렉트를 지원한다.
- `redirect:/basic/items/{itemId}"` 컨트롤러에 매핑된 @PathVariable 의 값은 redirect 에도 사용 할 수 있다.

## PRG Post/Redirect/Get

![리다이렉트 없는 처리의 문제점](img/prg-issue.png)

1. 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add + 상품 데이터를 서버로 전송한다. 
2. 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add + 상품 데이터를 서버로 다시 전송하게 된다.
3. 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.

![PRG](img/prg.png)

- 해결책(`PRG Post/Redirect/Get`) : 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출

### RedirectAttributes

- RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVarible, 쿼리 파라미터까지 처리해준다.
- `${param.status}` : 타임리프에서 쿼리 파라미터를 편리하게 조회하는 기능
  - 원래는 컨트롤러에서 모델에 직접 담고 값을 꺼내야 한다. 그런데 쿼리 파라미터는 자주 사용해서 타임리프에서 직접 지원

## 타임리프 스프링 통합

- 기본 메뉴얼 : https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
- 스프링 통합 메뉴얼 : https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html
- 스프링 부트가 제공하는 타임리프 설정 :
https://docs.spring.io/spring-boot/docs/current/reference/html/appendix-applicationproperties.html#common-application-properties-templating

