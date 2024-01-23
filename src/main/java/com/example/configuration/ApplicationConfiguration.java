package com.example.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Objects;

@Configuration
class ApplicationConfiguration {
    Dotenv dotenv = Dotenv.load();
    private final String DB_NAME = Objects.requireNonNull(dotenv.get("MONGODB_NAME"));

    Datastore datastore;


    @Bean
    @Scope("singleton")
    public Datastore datastore(MongoClient mongoClient) {
        this.datastore = Morphia.createDatastore(mongoClient, DB_NAME);

        return this.datastore;
    }

    @Bean
    public MongoClient mongoClient() {
        String DB_URI = Objects.requireNonNull(dotenv.get("MONGODB_URI"));
        return MongoClients.create(DB_URI);
    }

    @Bean
    MongoOperations mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, DB_NAME);
    }



}