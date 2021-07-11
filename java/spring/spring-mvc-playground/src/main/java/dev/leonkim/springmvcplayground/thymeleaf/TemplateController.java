package dev.leonkim.springmvcplayground.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/template")
public class TemplateController {

    @GetMapping("fragment")
    public String template() {
        return "thymeleaf/template/fragment/fragmentMain";
    }
}
