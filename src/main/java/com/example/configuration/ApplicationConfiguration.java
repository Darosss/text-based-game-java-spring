package com.example.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import java.util.Objects;

@Configuration
class ApplicationConfiguration {
    Dotenv dotenv = Dotenv.load();
    @Bean
    MongoClient mongoClient() {
        String DB_URI = Objects.requireNonNull(dotenv.get("MONGODB_URI"));
        return MongoClients.create(DB_URI);
    }

    @Bean
    MongoOperations mongoTemplate(MongoClient mongoClient) {
        String DB_NAME = Objects.requireNonNull(dotenv.get("MONGODB_NAME"));
        return new MongoTemplate(mongoClient, DB_NAME);
    }



}