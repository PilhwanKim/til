package dev.leonkim.springcoreadvanced.app.v4;

import dev.leonkim.springcoreadvanced.trace.TraceStatus;
import dev.leonkim.springcoreadvanced.trace.logtrace.LogTrace;
import dev.leonkim.springcoreadvanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {
    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };
        template.execute("OrderService.orderItem()");
    }
}
