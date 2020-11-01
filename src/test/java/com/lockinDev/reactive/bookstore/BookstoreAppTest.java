
package com.lockinDev.reactive.bookstore.api;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookstoreAppTest {
	private static Logger logger = LoggerFactory.getLogger(BookApiTest.class);

	@Autowired
	private WebTestClient testClient;

	@Test
	void shouldFindByIsbn(){
		testClient.get()
				.uri(uriBuilder -> uriBuilder.path("/book/isbn/{isbn}").build("9781484230042"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Book.class)
				.consumeWith(responseEntity -> {
					logger.debug("Response: {}", responseEntity);
					Book book = responseEntity.getResponseBody();
					assertAll("book", () ->
					{
						assertNotNull(book);
						assertAll("book",
								() -> assertNotNull(book.getTitle()),
								() -> assertNotEquals("", book.getAuthor()));
					});
				});
	}

	@Test
	void shouldFindByIsbnNot(){
		testClient.get()
				.uri(uriBuilder -> uriBuilder.path("/book/isbn/{isbn}").build("978148423test"))
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void shouldCreateABook() {
		Book book = new Book();
		book.setTitle("TDD for dummies");
		book.setAuthor("Test User");
		book.setPrice(BigDecimal.valueOf(40.99));
		book.setIsbn("12232434324");
		book.setCategory("test");

		testClient.post().uri("/book/isbn")
				.body(Mono.just(book), Book.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectHeader().exists("Location")
				.expectBody(Book.class)
				.consumeWith(responseEntity -> {
					logger.debug("Response: {}", responseEntity);
					assertAll("book", () ->
					{
						assertNotNull(book);
						assertAll("book",
								() -> assertNotNull(book.getIsbn()),
								() -> assertEquals("test", book.getCategory()));
					});
				});
	}


}
