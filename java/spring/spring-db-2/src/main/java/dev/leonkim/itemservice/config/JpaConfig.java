package dev.leonkim.itemservice.config;


import dev.leonkim.itemservice.repository.ItemRepository;
import dev.leonkim.itemservice.repository.jpa.JpaItemRepositoryV1;
import dev.leonkim.itemservice.service.ItemService;
import dev.leonkim.itemservice.service.ItemServiceV1;
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
        return new JpaItemRepositoryV1(em);
    }

}
