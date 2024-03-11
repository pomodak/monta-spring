package com.hororok.monta.repository;

import com.hororok.monta.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    Optional<Item> findOneById(int itemId);

    List<Item> findAllByItemType(String itemType);
}
