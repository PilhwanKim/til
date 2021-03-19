package dev.leonkim.springplaygroundservlet.web.frontcontroller.v1.controller;

import dev.leonkim.springplaygroundservlet.web.frontcontroller.v1.ControllerV1;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MemberFormControllerV1 implements ControllerV1 {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        // forward() - 서버 내에서 지정한 jsp 호출함(제어권을 넘김)
        dispatcher.forward(request, response);
    }
}
