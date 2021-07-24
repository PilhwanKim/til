package hello.springcore;

import hello.springcore.discount.DiscountPolicy;
import hello.springcore.discount.FixDiscountPolicy;
import hello.springcore.discount.RateDiscountPolicy;
import hello.springcore.member.MemberRepository;
import hello.springcore.member.MemberService;
import hello.springcore.member.MemberServiceImpl;
import hello.springcore.member.MemoryMemberRepository;
import hello.springcore.order.OrderService;
import hello.springcore.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 객체의 생성과 연결(의존관계 주입, Dependency Injection)은 AppConfig 가 담당한다.
 * DIP 의 완성이 가능하려면 AppConfig 의 역할을 분리해야 한다.
 */
// @Configuration 주석 처리하고 ConfigurationSingletonTest 실행결과 보기
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        // 주석 풀고 ConfigurationSingletonTest 실행해서 확인해보자
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        // 주석 풀고 ConfigurationSingletonTest 실행해서 확인해보자
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        // 주석 풀고 ConfigurationSingletonTest 실행해서 확인해보자
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        // 할인정책을 변경하려면 AppConfig 여기만 변경하면 된다.
//        return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }

}
