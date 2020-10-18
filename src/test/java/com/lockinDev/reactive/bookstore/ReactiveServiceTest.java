
package com.lockinDev.reactive.bookstore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.lockinDev.reactive.bookstore.Book;
import com.lockinDev.reactive.bookstore.BookService;
import com.lockinDev.reactive.bookstore.DataInitializer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReactiveServiceTest {

	private static final  Logger logger = LoggerFactory.getLogger(ReactiveServiceTest.class);
	@Autowired
	BookService bookService;

	@Test
	void shouldReadAllPersons() {
		bookService.findAll()
				.as(StepVerifier::create)
				.expectNextCount(3)
				.verifyComplete();
	}

	@Test
	void shouldReturnOne(){
		Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", DataInitializer.Category.WEB);
		bookService.findByIsbn("9780132350884").as(StepVerifier::create)
				.expectSubscription()
				.expectNext(book)
				.verifyComplete();
	}

	@Test
	void bogus(){
		Flux<String> items =  Flux.just("a", "b", "c");
		items.transform(s -> addLogging(s)).subscribe();
		//addLogging(items);
		//items.doOnNext(it -> System.out.printf("Received "+ it)). doOnError(e -> e.printStackTrace()).subscribe();
		items.subscribe();
	}

	static Flux<String>  addLogging(Flux<String> flux) {
		return flux.doOnNext(it -> System.out.printf("Received "+ it)). doOnError(e -> e.printStackTrace());
	}

}
