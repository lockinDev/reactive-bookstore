
package com.lockinDev.reactive.bookstore.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.service.BookstoreService;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;

import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;


/**
 * Created by lockinDev on 03/07/2020
 */
@Component
public class BookHandler {

	private BookstoreService bookstoreService;

	public HandlerFunction<ServerResponse> list;
	public HandlerFunction<ServerResponse> random;
	public HandlerFunction<ServerResponse> delete;

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
		var criteriaMono = serverRequest.bodyToMono(BookSearchCriteria.class);
		return criteriaMono.log()
				.flatMap(criteria -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(bookstoreService.findBooks(criteria), Book.class));
	}

	public Mono<ServerResponse> detail(ServerRequest request) {
		Mono<Book> bookMono = bookstoreService.findBookByIsbn(request.pathVariable("isbn"));
		return bookMono
				.flatMap(book -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.bodyValue(Book.class))
					.switchIfEmpty(ServerResponse.notFound().build());
	}
}
