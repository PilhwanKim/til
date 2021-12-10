package dev.leonkim.proxy.config.v6_aop;

import dev.leonkim.proxy.config.AppV1Config;
import dev.leonkim.proxy.config.AppV2Config;
import dev.leonkim.proxy.config.v6_aop.aspect.LogTraceAspect;
import dev.leonkim.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

}
