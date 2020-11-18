
package com.lockinDev.reactive.bookstore.web;

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

/**
 * Created by lockinDev on 29/07/2020
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookstoreWebTest {
	private static Logger logger = LoggerFactory.getLogger(BookstoreWebTest.class);

	@Autowired
	private  WebTestClient testClient;

	@Test
	public void shouldReturnBook(){
		testClient.get()
				.uri(uriBuilder -> uriBuilder.path("/book/isbn/{isbn}").build("9781484237779"))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.title").isNotEmpty()
				.jsonPath("$.author").isEqualTo("lockinDev");
	}

	@Test
	public void shouldReturnTwoBooks(){
		BookSearchCriteria criteria = new BookSearchCriteria();
		criteria.setCategory(Book.Category.JAVA);

		testClient.post()
			.uri("/book/search")
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(criteria), BookSearchCriteria.class)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Book.class)
				.hasSize(2);
	/*.consumeWith(
				result -> {
					assertEquals(2, result.getResponseBody().size());
					result.getResponseBody().forEach(p -> logger.info("All: {}",p));
				});*/
	}

}
