package dev.leonkim.proxy.config.v5_autoproxy;

import dev.leonkim.proxy.config.AppV1Config;
import dev.leonkim.proxy.config.AppV2Config;
import dev.leonkim.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import dev.leonkim.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

// 동일한 어드바이저 중복 적용을 막기 위해 @Bean 은 한족을 지운다.
//    @Bean
    public Advisor getAdvisor1(LogTrace logTrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    // 매우 정밀한 포인트컷이 필요하다.
    @Bean
    public Advisor getAdvisor2(LogTrace logTrace) {
        //pointcut - AspectJ 포인트컷 표현식을 적용할 수 있다.
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* dev.leonkim.proxy.app..*(..)) && !execution(* dev.leonkim.proxy.app..noLog(..)) ");
        // * : 모든 반환 타입
        // hello.proxy.app.. : 해당 패키지와 그 하위 패키지
        // *(..) : * 모든 메서드 이름, (..) 파라메터는 상관 없음
        // && : and 조건
        // ! : 반대

        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

}
