package dev.leonkim.proxy;

import dev.leonkim.proxy.config.AppV1Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(AppV1Config.class)
@SpringBootApplication(scanBasePackages = "dev.leonkim.proxy.app")
public class SpringCoreAdvanced2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringCoreAdvanced2Application.class, args);
	}

}
