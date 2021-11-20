package dev.leonkim.springcoreadvanced.trace.callback;

public interface TraceCallback<T> {
    T call();
}
