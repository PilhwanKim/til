package dev.leonkim.itemservice.repository.v2;

import dev.leonkim.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
