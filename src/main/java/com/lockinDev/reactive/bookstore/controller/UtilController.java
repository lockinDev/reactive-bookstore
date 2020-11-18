
package com.lockinDev.reactive.bookstore.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.service.BookstoreService;

import reactor.core.publisher.Flux;

/**
 * Created by lockinDev on 29/07/2020
 */
@RestController
public class UtilController {
	private BookstoreService bookstoreService;

	public UtilController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@RequestMapping(value = "/randomBookNews", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Book> getBeanNames() {
		return bookstoreService.randomBookNews();
	}

}
