package dev.leonkim.itemservice.service;

import dev.leonkim.itemservice.domain.Item;
import dev.leonkim.itemservice.repository.ItemSearchCond;
import dev.leonkim.itemservice.repository.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item save(Item item);

    void update(Long itemId, ItemUpdateDto updateParam);

    Optional<Item> findById(Long id);

    List<Item> findItems(ItemSearchCond itemSearch);
}
