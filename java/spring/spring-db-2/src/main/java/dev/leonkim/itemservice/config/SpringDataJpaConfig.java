package dev.leonkim.itemservice.config;


import dev.leonkim.itemservice.repository.ItemRepository;
import dev.leonkim.itemservice.repository.jpa.JpaItemRepositoryV1;
import dev.leonkim.itemservice.repository.jpa.JpaItemRepositoryV2;
import dev.leonkim.itemservice.repository.jpa.SpringDataJpaItemRepository;
import dev.leonkim.itemservice.service.ItemService;
import dev.leonkim.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {

    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV2(springDataJpaItemRepository);
    }

}
