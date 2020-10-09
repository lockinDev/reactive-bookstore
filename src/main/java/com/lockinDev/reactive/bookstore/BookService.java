package com.lockinDev.reactive.bookstore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface BookService {
	Mono<Book> findByIsbn(String isbn);

	Flux<Book> findAll();

	Mono<Book> save(Book book);

	Mono<Void> update(String id, Mono<Book> bookMono);

	Mono<Void> delete(String id);
}
