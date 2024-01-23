package com.example.characters.equipment;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CharacterEquipmentRepository extends MongoRepository<CharacterEquipment, String> {

    @NotNull
    @Override
    Optional<CharacterEquipment> findById(@NotNull String id);

}

