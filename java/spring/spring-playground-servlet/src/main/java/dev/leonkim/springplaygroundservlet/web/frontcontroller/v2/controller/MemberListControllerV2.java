package dev.leonkim.springplaygroundservlet.web.frontcontroller.v2.controller;

import dev.leonkim.springplaygroundservlet.domain.member.Member;
import dev.leonkim.springplaygroundservlet.domain.member.MemberRepository;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.MyView;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v2.ControllerV2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MemberListControllerV2 implements ControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        //Model 에 데이터 보관
        request.setAttribute("members", members);

        return new MyView("/WEB-INF/views/members.jsp");
    }
}
