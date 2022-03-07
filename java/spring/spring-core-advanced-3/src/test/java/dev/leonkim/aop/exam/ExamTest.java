package dev.leonkim.aop.exam;

import dev.leonkim.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@Slf4j
@Import(TraceAspect.class)
@SpringBootTest
public class ExamTest {

    @Autowired
    ExamService examService;

    @Test
    void test() {
        for (int i=0; i< 5; i++) {
            log.info("client request i={}", i);
            examService.request("data" + i);
        }
    }
}
