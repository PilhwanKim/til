package dev.leonkim.aop.internalcall;

import dev.leonkim.aop.internalcall.aop.CallLogAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@Slf4j
@Import({CallLogAspect.class})
@SpringBootTest(properties = "spring.main.allow-circular-references=true")
// 스프링 부트 2.6부터는 순환 참조를 기본적으로 금지하도록 정책이 변경됨
class CallServiceV1Test {

    @Autowired
    CallServiceV1 callServiceV1;

    @Test
    void external() {
        callServiceV1.external();
    }

}