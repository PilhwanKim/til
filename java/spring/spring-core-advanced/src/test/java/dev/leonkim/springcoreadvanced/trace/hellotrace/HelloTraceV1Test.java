package dev.leonkim.springcoreadvanced.trace.hellotrace;

import dev.leonkim.springcoreadvanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class HelloTraceV1Test {
    /**
     * 로그 추적기 V1 - 프로토타입 개발
     */

    @Test
    void begin_end() {
        HelloTraceV1 trace = new HelloTraceV1();
        TraceStatus status = trace.begin("hello");
        // 이 자리에 비즈니스 로직
        trace.end(status);
    }

    @Test
    void begin_exception() {
        HelloTraceV1 trace = new HelloTraceV1();
        TraceStatus status = trace.begin("hello");
        // 이 자리에 비즈니스 로직
        trace.exception(status, new IllegalStateException());
    }

}