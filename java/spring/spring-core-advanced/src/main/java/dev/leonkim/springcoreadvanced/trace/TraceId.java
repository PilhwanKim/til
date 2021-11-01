package dev.leonkim.springcoreadvanced.trace;

import java.util.UUID;

public class TraceId {
    /**
     * 트랜잭션 ID와 깊이를 표현
     */

    private String id;
    private int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        /**
         * ab99e16f-3cde-4d24-8241-256108c203a2 //생성된 UUID
         * ab99e16f //앞 8자리만 사용
         */
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return this.level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
