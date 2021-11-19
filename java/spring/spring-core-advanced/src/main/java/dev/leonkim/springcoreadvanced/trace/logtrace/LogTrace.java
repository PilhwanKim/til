package dev.leonkim.springcoreadvanced.trace.logtrace;

import dev.leonkim.springcoreadvanced.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);

}
