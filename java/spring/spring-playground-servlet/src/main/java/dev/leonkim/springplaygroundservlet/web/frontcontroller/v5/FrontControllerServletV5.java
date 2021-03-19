package dev.leonkim.springplaygroundservlet.web.frontcontroller.v5;

import dev.leonkim.springplaygroundservlet.web.frontcontroller.ModelView;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.MyView;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v5.adepter.ControllerV3HandlerAdapter;
import dev.leonkim.springplaygroundservlet.web.frontcontroller.v5.adepter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    // DI 구조까지 되면 완벽하다.
    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    // DI 구조까지 되면 완벽하다.
    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServletV5.service");

        // 1. 핸들러 조회
        Object handler = getHandler(request);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. 핸들러를 처리할 수 잇는 핸들러 어뎁터 조회
        MyHandlerAdapter adapter = getHandlerAdapter(handler);

        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        // 5. viewResolver 호출
        // 7. MyView 반환
        MyView view = viewResolver(viewName);

        // 8. rander(model) 호출
        view.render(mv.getModel(), request, response);
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다.");
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
