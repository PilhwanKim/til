package dev.leonkim;

import dev.leonkim.order.v0.OrderConfigV0;
import dev.leonkim.order.v1.OrderConfigV1;
import dev.leonkim.order.v2.OrderConfigV2;
import dev.leonkim.order.v3.OrderConfigV3;
import dev.leonkim.order.v4.OrderConfigV4;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(OrderConfigV4.class)
//@Import(OrderConfigV3.class)
//@Import(OrderConfigV2.class)
//@Import(OrderConfigV1.class)
//@Import(OrderConfigV0.class)
@SpringBootApplication(scanBasePackages = "dev.leonkim.controller")
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

    @Bean
    public InMemoryHttpExchangeRepository inMemoryHttpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }

}
