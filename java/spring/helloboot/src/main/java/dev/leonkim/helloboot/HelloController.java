package dev.leonkim.helloboot;

import java.util.Objects;

public class HelloController {
    public String hello(String name) {
        SimpleHelloService simpleHelloService = new SimpleHelloService();

        return simpleHelloService.sayHello(Objects.requireNonNull(name));
    }
}
