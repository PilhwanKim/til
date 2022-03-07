package dev.leonkim.aop.proxyvs;

import dev.leonkim.aop.member.MemberService;
import dev.leonkim.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * JDK 동적 프록시는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 없다.
 * CGLIB 프록시는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 있다.
 *
 * -> 왜 문제일까? 의존관계 주입시에 영향을 받는다.
 */
@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); //JDK 동적 프록시

        //프록시를 인터페이스로 케스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        log.info("proxy class={}", memberServiceProxy.getClass());

        //JDK 동적 프록시를 구현 클래스로 캐스팅 시도 실패, ClassCastException 에외 발생
        assertThrows(ClassCastException.class, () -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        });
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); //GCLIB 프록시

        //프록시를 인터페이스로 케스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        log.info("proxy class={}", memberServiceProxy.getClass());

        //JDK 동적 프록시를 구현 클래스로 캐스팅 시도 성공
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }

}
