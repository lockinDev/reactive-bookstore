
package com.lockinDev.reactive.bookstore.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.service.BookstoreService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lockinDev on 29/07/2020
 */
@Controller
public class BookDetailController {

	final BookstoreService bookstoreService;

	public BookDetailController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@ResponseBody
	@RequestMapping(value = "/book/id/{id}")
	public Mono<Book> getBookById(@PathVariable String id) {
		return bookstoreService.findBook(id);
	}

	@ResponseBody
	@RequestMapping(value = "/book/isbn/{isbn}")
	public Mono<Book> getBookByIsbn(@PathVariable String isbn) {
		return bookstoreService.findBookByIsbn(isbn);
	}

	@RequestMapping(value = "/book/detail/{bookId}")
	public String details(@PathVariable String bookId, Model model) {
		WebClient webClient = WebClient.create("http://localhost:8080/book");

		Flux<Book> bookFlux = webClient.get()
				.uri(
						uriBuilder -> uriBuilder.path("/id/{id}")
								.build(bookId)
				 )
				.retrieve()
				.bodyToMono(Book.class)
				.flux();

		IReactiveDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable( bookFlux,1);

		model.addAttribute("books", dataDriver);

		return "book/detail";
	}
}
