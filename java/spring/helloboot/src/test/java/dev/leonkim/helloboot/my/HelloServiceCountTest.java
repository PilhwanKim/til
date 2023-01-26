package dev.leonkim.helloboot.my;

import dev.leonkim.helloboot.HelloRepository;
import dev.leonkim.helloboot.HelloService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.IntStream;

@HellobootTest
public class HelloServiceCountTest {
    @Autowired
    HelloService helloService;
    @Autowired
    HelloRepository helloRepository;

    @Test
    void sayHelloIncreateCount() {
        IntStream.rangeClosed(1, 10).forEach( count -> {
            helloService.sayHello("Toby");
            Assertions.assertThat(helloRepository.countOf("Toby")).isEqualTo(count);
        });
    }
}
