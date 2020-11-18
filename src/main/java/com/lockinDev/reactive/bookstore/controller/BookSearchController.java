
package com.lockinDev.reactive.bookstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveSSEDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.service.BookstoreService;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;
import com.lockinDev.reactive.bookstore.util.ServiceProblems;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.lockinDev.reactive.bookstore.document.Book.Category.*;
import static com.lockinDev.reactive.bookstore.util.ServiceProblems.*;

import java.util.List;

/**
 * Created by lockinDev on 27/07/2020
 */
@Controller
public class BookSearchController {
	private final Logger logger = LoggerFactory.getLogger(BookSearchController.class);

	private BookstoreService bookstoreService;

	public BookSearchController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@ModelAttribute("categories")
	public List<String> getCategories() {
		return List.of(WEB, SPRING, JAVA);
	}

	@ModelAttribute
	public BookSearchCriteria criteria() {
		return new BookSearchCriteria();
	}

	@GetMapping(path = "/book/search")
	public String load(){
		return "book/search";
	}

	@RequestMapping( value = "/book/new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String newBooks(final Model model){
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:8080/randomBookNews")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
				.defaultCookie("InternalCookie", "all")
				.build();

		Flux<Book> newReleases = webClient.get()
				.uri("/")
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response ->
						Mono.error( response.statusCode() == HttpStatus.UNAUTHORIZED
							? new ServiceDeniedException("You shall not pass!")
							: new ServiceDeniedException("Well.. this is unfortunate!"))
				)
				.onStatus(HttpStatus::is5xxServerError, response ->
						Mono.error(response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR
							? new ServiceDownException("Internal error!!")
							: new ServiceDownException("Well.. this is a mistery!"))
				)
				.bodyToFlux(Book.class);

		final IReactiveSSEDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable(newReleases, 1, "newBooks");

		model.addAttribute("newBooks", dataDriver);   // Flux wrapped in a DataDriver to avoid resolution
		// Will use the same "book/search" template, but only a fragment: the newBooks block.
		return "book/search :: #newBooks";
	}

	@RequestMapping( value = "/tech/news", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String techNews(final Model model){
		final WebClient webClient = WebClient.create("http://localhost:3000/techNews");

		Flux<String> newReleases = webClient.get().uri("/")
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response ->
						Mono.error( response.statusCode() == HttpStatus.UNAUTHORIZED
								? new ServiceProblems.ServiceDeniedException("You shall not pass!")
								: new ServiceProblems.ServiceDeniedException("Well.. this is unfortunate!"))
				)
				.onStatus(HttpStatus::is5xxServerError, response ->
						Mono.error(response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR
								? new ServiceProblems.ServiceDownException("This is SpartAAA!!")
								: new ServiceProblems.ServiceDownException("Well.. this is a mystery!"))
				)
				.bodyToFlux(String.class);

		final IReactiveSSEDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable(newReleases, 1, "techNews");

		model.addAttribute("techNews", dataDriver);
		return "book/search :: #techNews";
	}
}

