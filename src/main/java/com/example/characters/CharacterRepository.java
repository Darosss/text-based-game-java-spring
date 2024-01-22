package com.example.characters;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface CharacterRepository extends MongoRepository<Character, String> {
    @NotNull
    @Override
    Optional<Character> findById(@NotNull String id);

    @Query("{ 'user': ?0 }")
    Optional<Character> findOneByUserId(String userId);

}
