package dev.leonkim.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// MySprintBootMain 를 실행할 때 주석처리한다.
//@Configuration
public class HelloConfig {

    @Bean
    public HelloController helloController() {
        return new HelloController();
    }
}
