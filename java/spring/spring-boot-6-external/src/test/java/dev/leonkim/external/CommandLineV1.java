package dev.leonkim.external;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandLineV1 {
    public static void main(String[] args) {
        // 다음과 같이 실행. key=value 형식이 아니라 번거로움
        // java -jar external-0.0.1-SNAPSHOT.jar url=devdb username=dev_user password=dev_pw
        for (String arg: args) {
            log.info("arg {}",arg);
        }
    }
}
