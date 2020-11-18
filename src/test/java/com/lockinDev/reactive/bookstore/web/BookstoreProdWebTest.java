
package com.lockinDev.reactive.bookstore.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Created by lockinDev on 30/07/2020
 * Suggestion: Start the application then comment the @Disabled annotation and run this test.
 */
@Disabled
public class BookstoreProdWebTest {

	private final WebTestClient testClient = WebTestClient
			.bindToServer()
			.baseUrl("http://localhost:8080")
			.build();

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

}
