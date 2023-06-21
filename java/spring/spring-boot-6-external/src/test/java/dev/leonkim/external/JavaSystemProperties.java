package dev.leonkim.external;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class JavaSystemProperties {
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        for (Object key : properties.keySet()) {
            log.info("prop {}={}", key, System.getProperty(String.valueOf(key)));
        }

        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        // 실제로 다음과 같이 자바 시스템 속성 넣기
        // java -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw -jar external-0.0.1-SNAPSHOT.jar
        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
    }
}
