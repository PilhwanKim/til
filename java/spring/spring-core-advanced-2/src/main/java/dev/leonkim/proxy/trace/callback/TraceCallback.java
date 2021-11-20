package dev.leonkim.proxy.trace.callback;

public interface TraceCallback<T> {
    T call();
}
