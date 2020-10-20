
package com.lockinDev.reactive.bookstore.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.service.BookstoreService;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;

import reactor.core.publisher.Flux;

import static com.lockinDev.reactive.bookstore.document.Book.Category.*;

import java.util.Collection;
import java.util.List;


@Controller
public class BookSearchController {

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

}
