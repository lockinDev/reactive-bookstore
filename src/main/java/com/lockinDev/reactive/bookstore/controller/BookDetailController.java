
package com.lockinDev.reactive.bookstore.controller;

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


@Controller
public class BookDetailController {

	final BookstoreService bookstoreService;

	public BookDetailController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@RequestMapping(value = "/book/detail/{bookId}")
	public String details(@PathVariable String bookId, Model model) {

		Flux<Book> bookFlux = bookstoreService.findBook(bookId).flux();

		IReactiveDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable( bookFlux,1);

		model.addAttribute("books", dataDriver);

		return "book/detail";
	}
}
