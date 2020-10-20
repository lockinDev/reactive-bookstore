
package com.lockinDev.reactive.bookstore.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.service.BookstoreService;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;
import com.lockinDev.reactive.bookstore.util.validation.BookValidator;

import reactor.core.publisher.Mono;

import javax.validation.ValidationException;
import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;



@Component
public class BookHandler {
	private final Logger logger = LoggerFactory.getLogger(BookHandler.class);

	private BookstoreService bookstoreService;
	private final Validator validator = new BookValidator();

	public final HandlerFunction<ServerResponse> list;
	public final HandlerFunction<ServerResponse> random;
	public final HandlerFunction<ServerResponse> delete;

	public BookHandler(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
		list = serverRequest -> ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(bookstoreService.findBooks(new BookSearchCriteria()), Book.class);

		random = serverRequest ->ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(bookstoreService.findRandomBooks(), Book.class);

		delete = serverRequest -> ServerResponse.noContent()
				.build(bookstoreService.deleteBook(serverRequest.pathVariable("isbn")));
	}

	public Mono<ServerResponse> search(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BookSearchCriteria.class)
				.flatMap(criteria -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(bookstoreService.findBooks(criteria), Book.class));
	}

	public HandlerFunction<ServerResponse> update = serverRequest -> ServerResponse.noContent()
			.build(bookstoreService.updateByIsbn(serverRequest.pathVariable("isbn"), serverRequest.bodyToMono(Book.class)));

	public Mono<ServerResponse> create(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(Book.class)
				.flatMap(this::validate)
					.flatMap(book -> bookstoreService.addBook(book))
					.flatMap(book -> ServerResponse.created(URI.create("/book/isbn/" + book.getIsbn()))
						.contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error));
	}

	private Mono<Book> validate(Book book) {
		Validator validator = new BookValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		validator.validate(book, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors().toString());
		}
		return Mono.just(book);
	}


	public Mono<ServerResponse> findOne(ServerRequest request) {
		return bookstoreService.findBookByIsbn(request.pathVariable("isbn"))
				.flatMap(book -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
