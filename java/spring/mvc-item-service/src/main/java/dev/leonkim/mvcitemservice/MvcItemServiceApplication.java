package dev.leonkim.mvcitemservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class MvcItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvcItemServiceApplication.class, args);
    }

    // 스프링 부트를 쓰면 굳이 선언 필요 없다! -> 자동으로 등록해줌
//    @Bean
//    public MessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new
//                ResourceBundleMessageSource();
//        messageSource.setBasenames("messages", "errors");
//        messageSource.setDefaultEncoding("utf-8");
//        return messageSource;
//    }
}
