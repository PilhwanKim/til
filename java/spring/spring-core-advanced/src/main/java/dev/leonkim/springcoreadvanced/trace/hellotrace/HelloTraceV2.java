package dev.leonkim.springcoreadvanced.trace.hellotrace;

import dev.leonkim.springcoreadvanced.trace.TraceId;
import dev.leonkim.springcoreadvanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HelloTraceV2 {
    /**
     * HelloTraceV2 는 기존 코드인 HelloTraceV1 과 같고, beginSync(..) 가 추가됨
     */
    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public TraceStatus begin(String message) {
        /**
         * 로그를 시작한다.
         * 로그 메시지를 파라미터로 받아서 시작 로그를 출력한다.
         * 응답 결과로 현재 로그의 상태인 TraceStatus 를 반환한다.
         */
        TraceId traceId = new TraceId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
        return new TraceStatus(traceId, startTimeMs, message);
    }

    public TraceStatus beginSync(TraceId beforeTraceId, String message) {
        /**
         * 기존 TraceId 에서 createNextId() 를 통해 다음 ID를 구한다. createNextId() 의 TraceId 생성 로직은 다음과 같다.
         * 트랜잭션ID는 기존과 같이 유지한다.
         * 깊이를 표현하는 Level은 하나 증가한다. ( 0 -> 1 )
         */
        TraceId nextId = beforeTraceId.createNextId();
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);
        return new TraceStatus(nextId, startTimeMs, message);
    }

    public void end(TraceStatus status) {
        /**
         * 로그를 정상 종료한다.
         * 파라미터로 시작 로그의 상태( TraceStatus )를 전달 받는다. 이 값을 활용해서 실행 시간을 계산하고, 종료시에도 시작할 때와 동일한 로그 메시지를 출력할 수 있다.
         * 정상 흐름에서 호출한다.
         */
        complete(status, null);
    }

    public void exception(TraceStatus status, Exception e) {
        /**
         * 로그를 예외 상황으로 종료한다.
         * TraceStatus, Exception 정보를 함께 전달 받아서 실행시간, 예외 정보를 포함한 결과 로그를 출력한다.
         * 예외가 발생했을 때 호출한다.
         */
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        /**
         * end(), exception() 의 요청 흐름을 한곳에서 편리하게 처리한다. 실행 시간을 측정하고 로그를 남긴다.
         */
        long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms, ex={}", traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs,
                    e.toString());
        }
    }

    private String addSpace(String prefix, int level) {
        /**
         * prefix: -->
         *      level 0:
         *      level 1: |-->
         *      level 2: | |-->
         * prefix: <--
         *      level 0:
         *      level 1: |<--
         *      level 2: | |<--
         * prefix: <X-
         *      level 0:
         *      level 1: |<X-
         *      level 2: | |<X-
         */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append( (i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }
}
