package dev.leonkim.hello.config;

//import dev.leonkim.memory.MemoryCondition;
import dev.leonkim.memory.MemoryController;
import dev.leonkim.memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Conditional(MemoryCondition.class)
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryConfig {

    @Bean
    public MemoryController memoryController() {
        return new MemoryController(memoryFinder());
    }

    @Bean
    public MemoryFinder memoryFinder() {
        return new MemoryFinder();
    }
}
