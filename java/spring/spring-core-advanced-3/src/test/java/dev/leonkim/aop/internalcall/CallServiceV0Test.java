package dev.leonkim.aop.internalcall;

import dev.leonkim.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({CallLogAspect.class})
@SpringBootTest
class CallServiceV0Test {

    @Autowired CallServiceV0 callServiceV0;

    @Test
    void external() {
        // 내부 internal() 호출시 AOP 가 적용되지 않는다.
        callServiceV0.external();
    }

    @Test
    void internal() {
        callServiceV0.internal();
    }
}