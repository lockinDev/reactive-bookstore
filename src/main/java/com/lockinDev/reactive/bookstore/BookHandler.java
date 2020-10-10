
package com.lockinDev.reactive.bookstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;


@Component
public class BookHandler {

	private final BookService bookService;
	public HandlerFunction<ServerResponse> list;
	public HandlerFunction<ServerResponse> delete;

	public BookHandler(BookService bookService) {
		this.bookService = bookService;

		
		list = serverRequest -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(bookService.findAll(), Book.class);

		
		delete = serverRequest -> ServerResponse.noContent()
				.build(bookService.delete(serverRequest.pathVariable("id")));
	}

	
	public Mono<ServerResponse> findByIsbn(ServerRequest serverRequest) {
		Mono<Book> bookMono = bookService.findByIsbn(serverRequest.pathVariable("isbn"));
		return bookMono
				.flatMap(book -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	
	public Mono<ServerResponse> save(ServerRequest serverRequest) {
		Mono<Book> bookMono =  serverRequest.bodyToMono(Book.class).doOnNext(bookService::save);
		return bookMono
				.flatMap(book -> ServerResponse.created(URI.create("/books/" + book.getId()))
						.contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
