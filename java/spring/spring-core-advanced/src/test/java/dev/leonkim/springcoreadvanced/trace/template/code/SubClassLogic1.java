package dev.leonkim.springcoreadvanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic1 extends AbstractTemplate {
    @Override
    protected void call() {
        // 변하는 부분을 오버라이딩
        log.info("비즈니스 로직1 실행");
    }
}
