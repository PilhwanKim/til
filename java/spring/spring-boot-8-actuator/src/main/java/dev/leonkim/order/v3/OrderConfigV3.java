package dev.leonkim.order.v3;

import dev.leonkim.order.OrderService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV3 {

    @Bean
    public OrderService orderService(MeterRegistry meterRegistry) {
        return new OrderServiceV3(meterRegistry);
    }
}
