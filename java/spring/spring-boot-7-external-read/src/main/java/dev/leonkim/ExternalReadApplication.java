package dev.leonkim;

import dev.leonkim.config.MyDataSourceConfigV1;
import dev.leonkim.config.MyDataSourceConfigV2;
import dev.leonkim.config.MyDataSourceEnvConfig;
import dev.leonkim.config.MyDataSourceValueConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

//@Import(MyDataSourceEnvConfig.class)
//@Import(MyDataSourceValueConfig.class)
//@Import(MyDataSourceConfigV1.class)
@Import(MyDataSourceConfigV2.class)
@SpringBootApplication(scanBasePackages = "dev.leonkim.datasource")
//@ConfigurationPropertiesScan({"dev.leonkim"})
public class ExternalReadApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }

}
