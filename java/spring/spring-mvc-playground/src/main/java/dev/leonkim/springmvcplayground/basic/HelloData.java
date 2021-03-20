package dev.leonkim.springmvcplayground.basic;

import lombok.Data;

// @Data = @Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 자동 적용
@Data
public class HelloData {
    private String username;
    private int age;
}
