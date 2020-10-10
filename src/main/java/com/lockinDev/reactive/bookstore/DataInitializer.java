
package com.lockinDev.reactive.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;

import com.lockinDev.reactive.bookstore.DataInitializer.Category;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class DataInitializer {

	public static class Category {
		public static final String SPRING = "Spring";
		public static final String JAVA = "Java";
		public static final String WEB = "Web";
	}

	@Autowired ReactiveMongoOperations operations;

	private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	private final BookRepository bookRepo;

	public DataInitializer(BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		  operations.collectionExists(Book.class)
				.flatMap(exists -> exists ? operations.dropCollection(Book.class) : Mono.just(exists))
				.then(operations.createCollection(Book.class, CollectionOptions.empty())).subscribe(
					data -> logger.info("Collection saved: {}" , data ),
					error -> logger.info("Opps!"),
					() -> logger.info("Collection initialized!")
					);

		logger.info(" -->> Starting collection initialization...");
		bookRepo.saveAll(	Flux.just(
				new Book("Clean Code", "Robert C. Martin", "9780132350884", Category.WEB),
				new Book("Agile Software Development",  "Robert C. Martin", "9780135974445", Category.SPRING),
				new Book("Effective Java",  "Joshua Bloch", "9780321356680", Category.JAVA)
		)).subscribe(
				data -> logger.info("Saved {} books", data),
				error -> logger.error("Oops!"),
				() -> logger.info("Collection initialized!")
		);
	}
}
