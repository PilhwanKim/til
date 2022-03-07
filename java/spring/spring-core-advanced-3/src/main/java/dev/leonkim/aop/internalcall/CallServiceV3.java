package dev.leonkim.aop.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 프록시와 내부 호출 문제 대안 3 : 구조 변경
 * 구조를 분리함
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

    private final InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal(); //외부 메서드 호출
    }

}
