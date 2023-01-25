package dev.leonkim.helloboot;


import dev.leonkim.config.MySpringBootApplication;
import org.springframework.boot.SpringApplication;

@MySpringBootApplication
public class HellobootApplication {

	public static void main(String[] args) {
		SpringApplication.run(HellobootApplication.class, args);
//		MySpringApplication.run(HellobootApplication.class, args);
	}

}
