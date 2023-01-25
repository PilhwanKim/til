package dev.leonkim.helloboot;

import java.util.Objects;

public class HelloController {
    final private HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    public String hello(String name) {
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}
