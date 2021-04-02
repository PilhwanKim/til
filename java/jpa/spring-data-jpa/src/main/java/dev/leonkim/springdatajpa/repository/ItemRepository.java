package dev.leonkim.springdatajpa.repository;

import dev.leonkim.springdatajpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
