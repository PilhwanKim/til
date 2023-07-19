package dev.leonkim.order.v1;

import dev.leonkim.order.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV1 {

    @Bean
    public OrderService orderService(MeterRegistry registry) {
        return new OrderServiceV1(registry);
    }
}
