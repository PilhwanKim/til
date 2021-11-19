package dev.leonkim.springcoreadvanced;

//import dev.leonkim.springcoreadvanced.trace.logtrace.FieldLogTrace;
import dev.leonkim.springcoreadvanced.trace.logtrace.LogTrace;
import dev.leonkim.springcoreadvanced.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        // singleton 으로 bean 이 등록된다.
//        return new FieldLogTrace();
        return new ThreadLocalLogTrace();
    }
}
