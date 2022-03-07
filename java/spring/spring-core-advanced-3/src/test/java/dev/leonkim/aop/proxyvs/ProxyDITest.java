package dev.leonkim.aop.proxyvs;

import dev.leonkim.aop.member.MemberService;
import dev.leonkim.aop.member.MemberServiceImpl;
import dev.leonkim.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * JDK 동적 프록시는 대상 객체인 MemberServiceImpl 타입에 의존관계를 주입 할 수 없다.
 * CGLIB 프록시는 대상 객체인 MemberServiceImpl 타입에 의존관계를 주입 할 수 있다.
 *
 * -> 의존관계 주입을 하려면 캐스팅이 선행 되어야 하기 때문
 */

@Slf4j
// 주석처리로 어떻게 바뀌는지 확인해 보자.
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) //CGLIB 프록시
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) //JDK 동적 프록시
@Import(ProxyDIAspect.class)
public class ProxyDITest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberServiceImpl memberServiceImpl;

    @Test
    void go() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }

}
