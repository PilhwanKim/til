package dev.leonkim.springplaygroundservlet.web.frontcontroller.v1.controller;

import dev.leonkim.springplaygroundservlet.domain.member.Member;
import dev.leonkim.springplaygroundservlet.domain.member.MemberRepository;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v1.ControllerV1;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberSaveControllerV1 implements ControllerV1 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model 에 데이터 보관
        request.setAttribute("member", member);

        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        // forward() - 서버 내에서 지정한 jsp 호출함(제어권을 넘김)
        dispatcher.forward(request, response);
    }
}
