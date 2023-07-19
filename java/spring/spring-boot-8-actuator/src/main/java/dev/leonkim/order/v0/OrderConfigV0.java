package dev.leonkim.order.v0;

import dev.leonkim.order.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV0 {

    @Bean
    public OrderService orderService() {
        return new OrderServiceV0();
    }
}
