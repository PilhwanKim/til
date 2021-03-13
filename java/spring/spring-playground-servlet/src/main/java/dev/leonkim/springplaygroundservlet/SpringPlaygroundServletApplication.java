package dev.leonkim.springplaygroundservlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class SpringPlaygroundServletApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPlaygroundServletApplication.class, args);
    }

}
