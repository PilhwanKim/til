package dev.leonkim;

import dev.leonkim.boot.MySpringApplication;
import dev.leonkim.boot.MySpringBootApplication;

@MySpringBootApplication
public class MySprintBootMain {
    public static void main(String[] args) {
        MySpringApplication.run(MySprintBootMain.class, args);
    }
}
