package com.example.items;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ItemRepository extends MongoRepository<Item, String> {

    @NotNull
    @Override
    Optional<Item> findById(@NotNull String id);
}
