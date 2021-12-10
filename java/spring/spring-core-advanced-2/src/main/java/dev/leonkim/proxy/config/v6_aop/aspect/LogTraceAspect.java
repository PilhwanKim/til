package dev.leonkim.proxy.config.v6_aop.aspect;

import dev.leonkim.proxy.trace.TraceStatus;
import dev.leonkim.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect // 에노테이션 기반 프록시 적용에 필요
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    @Around("execution(* dev.leonkim.proxy.app..*(..))") // 포인트컷(Pointcut) - 포인트컷 표현식을 넣음
    // execute 메서드 : 어드바이스(Advice) 역할
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toShortString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }

    }

}
