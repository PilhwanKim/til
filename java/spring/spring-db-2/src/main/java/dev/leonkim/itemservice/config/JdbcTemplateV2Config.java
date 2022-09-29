package dev.leonkim.itemservice.config;


import dev.leonkim.itemservice.repository.ItemRepository;
import dev.leonkim.itemservice.repository.jdbctemplate.JdbcTemplateItemRepositoryV2;
import dev.leonkim.itemservice.service.ItemService;
import dev.leonkim.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV2Config {

    private final DataSource dataSource;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV2(dataSource);
    }

}
