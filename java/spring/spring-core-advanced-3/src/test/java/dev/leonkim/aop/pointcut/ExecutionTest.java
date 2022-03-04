package dev.leonkim.aop.pointcut;

import dev.leonkim.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        //public java.lang.String dev.leonkim.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void exactMatch() {
        //접근제어자?: public
        //반환타입: String
        //선언타입?: hello.aop.member.MemberServiceImpl 메서드이름: hello
        //파라미터: (String)
        //예외?: 생략
        pointcut.setExpression("execution(public String dev.leonkim.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        // 접근제어자?: 생략
        // 반환타입: *
        // 선언타입?: 생략
        // 메서드이름: *
        // 파라미터: (..)
        // 예외?: 생략
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        // * 은 아무 값이 들어와도 된다는 뜻
        // 파라미터에서 .. 은 파라미터의 타입과 파라미터 수가 상관없다는 뜻
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* dev.leonkim.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatch2() {
        // 패키지 안에 있는것 모두 매칭 가능함
        pointcut.setExpression("execution(* dev.leonkim.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageExactMatchFalse() {
        pointcut.setExpression("execution(* dev.leonkim.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage1() {
        // . : 정확하게 해당 위치의 패키지
        //.. : 해당 위치의 패키지와 그 하위 패키지도 포함
        pointcut.setExpression("execution(* dev.leonkim.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2() {
        // . : 정확하게 해당 위치의 패키지
        //.. : 해당 위치의 패키지와 그 하위 패키지도 포함
        pointcut.setExpression("execution(* dev.leonkim.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeExactMatch() {
        // 타입 매칭 - 같은 타입
        pointcut.setExpression("execution(* dev.leonkim.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        // 타입 매칭 - 부모 타입 허용 (부모 : MemberService, 자식 : MemberServiceImpl)
        pointcut.setExpression("execution(* dev.leonkim.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* dev.leonkim.aop.member.MemberServiceImpl.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        // 타입 매칭 - 인터페이스에 선언한 메서드만 매칭이 된다.
        pointcut.setExpression("execution(* dev.leonkim.aop.member.MemberService.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
    }

    //String 타입
    //(String)
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //파라미터가 없어야 함
    //()
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    //(*) : 정확히 하나의 파라미터 허용, 모든 타입 허용
    //(Xxx)
    @Test
    void argsMatchNoStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //(..) : 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    //(), (Xxx), (Xxx, Xxx)
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    //(String), (String, Xxx), (String, Xxx, Xxx)
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

}
