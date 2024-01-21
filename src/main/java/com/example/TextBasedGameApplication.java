package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class TextBasedGameApplication {

	private static final Logger log = LoggerFactory.getLogger(TextBasedGameApplication.class);



	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(TextBasedGameApplication.class, args);
	}

}