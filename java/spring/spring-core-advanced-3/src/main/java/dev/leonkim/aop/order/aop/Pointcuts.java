package dev.leonkim.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // dev.leonkim.aop.order 페키지와 하위 페키지를 모두 포함
    @Pointcut("execution(* dev.leonkim.aop.order..*(..))") //pointcut expression
    public void allOrder() {} //pointcut signature

    // 클래스 이름 패턴이 *Service
    @Pointcut("execution(* *..*Service.*(..))") //pointcut expression
    public void allService() {} //pointcut signature

    //allOrder & allService
    @Pointcut("allOrder() & allService()")
    public void orderAndService() {}

}
