package com.example.items;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {

    @NotNull
    @Override
    Optional<Item> findById(@NotNull String id);


    @Query("{ 'type' : ?0 }")
    Optional<Item> findByItemType(ItemTypeEnum itemType);
}
