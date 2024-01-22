package com.example.users;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface UserRepository extends MongoRepository<User, String> {

    @NotNull
    @Override
    Optional<User> findById(@NotNull String id);
}
