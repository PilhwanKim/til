package dev.leonkim.itemservice.config;


import dev.leonkim.itemservice.repository.ItemRepository;
import dev.leonkim.itemservice.repository.jpa.JpaItemRepository;
import dev.leonkim.itemservice.repository.mybatis.ItemMapper;
import dev.leonkim.itemservice.repository.mybatis.MybatisItemRepository;
import dev.leonkim.itemservice.service.ItemService;
import dev.leonkim.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class JpaConfig {

    private final EntityManager em;

    public JpaConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepository(em);
    }

}
