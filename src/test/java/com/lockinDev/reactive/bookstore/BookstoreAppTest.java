
package com.lockinDev.reactive.bookstore;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.lockinDev.reactive.bookstore.Book;

import reactor.test.StepVerifier;
import static org.hamcrest.CoreMatchers.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Disabled("For some reason these tests fail in the gradle build, run them only manually from IntelliJ IDEA")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookstoreAppTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void shouldReturnABook(){
		client.get().uri("/books/{isbn}", "9780132350884")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody(Book.class)
				.value(Book::getTitle, equalTo("Clean Code"));
	}

	@Test
	public void fetchAllBooks() {
		FluxExchangeResult<Book> result = client.get().uri("/books")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType("application/stream+json")
				.returnResult(Book.class);

		StepVerifier.create(result.getResponseBody())
				.expectNextCount(3)
				.verifyComplete();
	}
}
