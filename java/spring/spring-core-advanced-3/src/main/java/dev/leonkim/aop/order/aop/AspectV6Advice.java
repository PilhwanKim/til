package dev.leonkim.aop.order.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {

    // @Around 만이 유일하게 ProceedingJoinPoint 를 인자로 받을 수 있다.
    // ProceedingJoinPoint 만이 proceed() 메서드를 호출 가능
    @Around("dev.leonkim.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            // @Before
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            // @AfterReturning
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            // @AfterThrowing
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        } finally {
            // @After
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }

    @Before("dev.leonkim.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    // returning = "result" Object 인자의 이름과 같이 지정해야 함
    // 호환되는 타입만 받을수 있다. String result String 과 String 하위 타입만 받을 수 있다.
    @AfterReturning(value = "dev.leonkim.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    // ex - 예외도 호환되는 타입만 받을수 있다.
    @AfterThrowing(value = "dev.leonkim.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Object ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex);
    }

    // 정상 & 예외 모두 일반적으로 리소스 해지에 사용
    @After(value = "dev.leonkim.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
