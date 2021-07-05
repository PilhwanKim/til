package dev.leonkim.springmvcplayground.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("text-basic")
    public String textBasic(Model model) {
        model.addAttribute("data", "Hello Spring!");
        return "thymeleaf/basic/text-basic";
    }

    @GetMapping("text-unescaped")
    public String textUnescapeds(Model model) {
        model.addAttribute("data", "Hello <b>Spring!</b>");
        return "thymeleaf/basic/text-unescaped";
    }
}
