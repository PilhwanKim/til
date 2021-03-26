package dev.leonkim.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SpringDataJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(UUID.randomUUID().toString());
//        return new AuditorAware<String>() {
//            @Override
//            public Optional<String> getCurrentAuditor() {
//                return Optional.of(UUID.randomUUID().toString());
//            }
//        };

    }

}
