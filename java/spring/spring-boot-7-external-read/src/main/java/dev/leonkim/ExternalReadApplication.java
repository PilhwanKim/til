package dev.leonkim;

import dev.leonkim.config.MyDataSourceEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(MyDataSourceEnvConfig.class)
@SpringBootApplication(scanBasePackages = "dev.leonkim.datasource")
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
