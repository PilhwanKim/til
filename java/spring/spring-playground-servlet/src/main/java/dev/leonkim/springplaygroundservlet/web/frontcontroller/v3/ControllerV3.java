package dev.leonkim.springplaygroundservlet.web.frontcontroller.v3;

import dev.leonkim.springplaygroundservlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);

}
