
package com.lockinDev.reactive.bookstore;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;


@EnableWebFlux
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.lockinDev.reactive.bookstore")
@ComponentScan(basePackages = { "com.lockinDev.reactive.bookstore"})
public class AppConfiguration {

	@Bean
	public MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost");
	}

	public ReactiveMongoDatabaseFactory mongoDbFactory() {
		return new SimpleReactiveMongoDatabaseFactory(mongoClient(), "bookstore");
	}

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(mongoDbFactory());
	}
}
