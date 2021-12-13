package dev.leonkim.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    // dev.leonkim.aop.order 페키지와 하위 페키지를 모두 포함
    @Pointcut("execution(* dev.leonkim.aop.order..*(..))") //pointcut expression
    private void allOrder() {} //pointcut signature

    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); //join point 시그니쳐
        return joinPoint.proceed();
    }
}
